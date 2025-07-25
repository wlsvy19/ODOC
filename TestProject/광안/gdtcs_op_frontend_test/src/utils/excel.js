import ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';
import dayjs from 'dayjs';
import { showMessage } from '@/utils/common';
import i18n from '@/plugins/i18n';

export const excelDownload = async (
  row,
  searchHeader,
  searchData,
  excelHeaders,
  data,
  sheetName = 'Sheet 1',
  fileNamePrefix = '광안대교 엑셀파일',
  addedRow,
) => {
  const { t } = i18n.global;
  try {
    const timestamp = dayjs().format('YYYYMMDD_HHmmss');
    const fileName = `${fileNamePrefix}_${timestamp}.xlsx`;

    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet(sheetName);

    worksheet.columns = excelHeaders.map((header) => ({
      header: header.title,
      key: header.key,
      width: header.excelWidth ? header.excelWidth : header.title.length * 5,
    }));

    /* 조회조건 표출 start */
    let prevObj = {
      startDate: false,
      tempDate: false,
      inputButton: false,
      type: '',
      text: '',
    };
    const initPrevObj = () => {
      prevObj = {
        startDate: false,
        tempDate: false,
        inputButton: false,
        type: '',
        text: '',
      };
    };
    const checkPeriod = (type, newObj) => {
      const isStartExist = prevObj['startDate'];
      const isTempExist = prevObj['tempDate'];
      const label = newObj.label.length > 1 ? `${newObj.label}: ` : `${newObj.label}`;
      const text = prevObj['text'].length !== 0 ? `${prevObj['text']} ${label} ${searchData[newObj.key]}` : `${label} ${searchData[newObj.key]}`;

      if (prevObj['type'] === '') prevObj['type'] = type;
      prevObj['text'] = text;

      if (type != 'inputButton') {
        if (!isStartExist) prevObj['startDate'] = true;
        else if (isStartExist && prevObj['type'] !== type) prevObj['tempDate'] = true;
        else if (isStartExist && prevObj['type'] === type) {
          if (isTempExist) {
            prevObj['tempDate'] = false;
            prevObj['type'] = type === 'date' ? 'time' : 'date';
          } else {
            initPrevObj();
            return `${text}, `;
          }
        }
      } else {
        if (prevObj[type]) {
          prevObj[type] = false;
          prevObj['text'] = '';
          return `${text}, `;
        } else {
          prevObj[type] = true;
          return null;
        }
      }
      return null;
    };
    const checkPrev = () => {
      const text = prevObj['text'];
      const isStartExist = prevObj['startDate'];
      initPrevObj();
      return isStartExist ? `${text}, ` : text;
    };

    const searchCondition = searchHeader
      .map((obj) => {
        let prevData = '';
        let data = `${obj.label}: ${searchData[obj.key] === '' ? '전체' : searchData[obj.key]}, `;
        if (obj.type == 'date') return checkPeriod('date', obj);
        else if (obj.type == 'time') return checkPeriod('time', obj);
        else if (obj.type == 'inputButton') return checkPeriod('inputButton', obj);
        else prevData = checkPrev();

        if (obj.type == 'select') {
          const selectValue = obj.option?.find((opt) => opt.value === searchData[obj.key]);
          data = obj.label != '' ? `${obj.label}: ${selectValue.title}, ` : `${selectValue.title}`;
        } else if (obj.type == 'checkbox') data = searchData[obj.key] === true ? `${obj.label}, ` : null;
        else if (obj.type == 'radio') {
          const radioValue = obj.option?.find((opt) => opt.value === searchData[obj.key]);
          data = `${radioValue.title}, `;
        } else if (obj.type == null) data = `${obj.label}, `;
        return prevData !== '' ? [prevData, data].join(' ') : data;
      })
      .filter((data) => data !== null);

    if (searchCondition.length === 0) searchCondition.push(checkPrev());

    worksheet.mergeCells('A1', `AZ1`);
    worksheet.getCell('A1').value = searchCondition.join('').replace(/,\s*$/, '');

    worksheet.getRow(1).eachCell((cell) => {
      cell.font = { bold: true };
      cell.alignment = { horizontal: 'left', vertical: 'middle' };
    });
    /* 조회조건 표출 end */

    let colCount = 0;
    // 타이틀이 1행 인 경우
    if (row === 1) {
      // 2행에 타이틀 표출
      worksheet.getRow(2).values = excelHeaders.map((header) => header.title);
      worksheet.getRow(2).eachCell((cell) => {
        cell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
        cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0086E5' } };
        cell.alignment = { horizontal: 'center', vertical: 'middle' };
        cell.border = {
          top: { style: 'thin' },
          left: { style: 'thin' },
          bottom: { style: 'thin' },
          right: { style: 'thin' },
        };
      });

      // 3행부터 데이터 표출
      data.forEach((item) => {
        const row = worksheet.addRow(item);
        row.eachCell({ includeEmpty: true }, (cell) => {
          if (typeof cell.value === 'number') {
            cell.numFmt = '#,##0'; // 숫자 포맷 설정
          }
          cell.alignment = { horizontal: 'center', vertical: 'middle' };
          cell.border = {
            top: { style: 'thin' },
            left: { style: 'thin' },
            bottom: { style: 'thin' },
            right: { style: 'thin' },
          };
        });
      });
      colCount = excelHeaders.length;
      // 타이틀이 2행인 경우
    } else if (row === 2) {
      let columnIndex = 1; // 엑셀 열 인덱스
      // 타이틀과 자식 타이틀을 순회하며 엑셀의 열 구조를 설정
      excelHeaders.forEach((header) => {
        const titleRow = worksheet.getRow(2);
        const titleCell = titleRow.getCell(columnIndex);

        // 2행에 부모 타이틀 표출, 타이틀 병합
        worksheet.mergeCells(2, columnIndex, 2, columnIndex + header.children.length - 1);
        titleCell.value = header.title;
        titleCell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
        titleCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0086E5' } };
        titleCell.alignment = { horizontal: 'center', vertical: 'middle' };
        titleCell.border = {
          top: { style: 'thin' },
          left: { style: 'thin' },
          bottom: { style: 'thin' },
          right: { style: 'thin' },
        };

        // 3행에 자식 타이틀 표출
        header.children.forEach((child) => {
          const cell = worksheet.getCell(3, columnIndex);
          cell.value = child.title;
          cell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
          cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0095FF' } };
          cell.alignment = { horizontal: 'center', vertical: 'middle' };
          cell.border = {
            top: { style: 'thin' },
            left: { style: 'thin' },
            bottom: { style: 'thin' },
            right: { style: 'thin' },
          };
          worksheet.getColumn(columnIndex).width = child.width ? child.width / 5 : child.title.length * 5;

          columnIndex++;
          colCount = columnIndex;
        });
      });

      // 4행부터 데이터 표출
      data.forEach((item, rowIndex) => {
        let dataColumnIndex = 1;
        excelHeaders.forEach((header) => {
          header.children.forEach((child) => {
            // 4행부터 데이터 표출
            const cell = worksheet.getCell(rowIndex + 4, dataColumnIndex);
            cell.value = item[child.key];
            if (typeof cell.value === 'number') {
              cell.numFmt = '#,##0'; // 숫자 포맷 설정
            }
            cell.alignment = { horizontal: 'center', vertical: 'middle' };
            cell.border = {
              top: { style: 'thin' },
              left: { style: 'thin' },
              bottom: { style: 'thin' },
              right: { style: 'thin' },
            };
            dataColumnIndex++; // 열 인덱스 증가
          });
        });
      });
    } else if (row === 'mix') {
      let columnIndex = 1; // 엑셀 열 인덱스

      // 타이틀과 자식 타이틀을 순회하며 엑셀의 열 구조를 설정
      excelHeaders.forEach((header) => {
        const titleRow = worksheet.getRow(2);
        const titleCell = titleRow.getCell(columnIndex);

        if (header.children) {
          // 2행에 부모 타이틀 표출, 타이틀 병합
          worksheet.mergeCells(2, columnIndex, 2, columnIndex + header.children.length - 1);
          titleCell.value = header.title;
          titleCell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
          titleCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0086E5' } };
          titleCell.alignment = { horizontal: 'center', vertical: 'middle' };
          titleCell.border = {
            top: { style: 'thin' },
            left: { style: 'thin' },
            bottom: { style: 'thin' },
            right: { style: 'thin' },
          };

          // 3행에 자식 타이틀 표출
          header.children.forEach((child) => {
            const cell = worksheet.getCell(3, columnIndex);
            cell.value = child.title;
            cell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
            cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0095FF' } };
            cell.alignment = { horizontal: 'center', vertical: 'middle' };
            cell.border = {
              top: { style: 'thin' },
              left: { style: 'thin' },
              bottom: { style: 'thin' },
              right: { style: 'thin' },
            };
            worksheet.getColumn(columnIndex).width = child.width ? child.width / 5 : child.title.length * 5;

            columnIndex++;
            colCount = columnIndex;
          });
        } else {
          // 2행과 3행에 타이틀 병합하여 표출
          worksheet.mergeCells(2, columnIndex, 3, columnIndex);
          titleCell.value = header.title;
          titleCell.font = { bold: true, color: { argb: 'FFFFFFFF' } };
          titleCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0086E5' } };
          titleCell.alignment = { horizontal: 'center', vertical: 'middle' };
          titleCell.border = {
            top: { style: 'thin' },
            left: { style: 'thin' },
            bottom: { style: 'thin' },
            right: { style: 'thin' },
          };
          worksheet.getColumn(columnIndex).width = header.width ? header.width / 5 : header.title.length * 5;
          columnIndex++;
        }
      });

      // 데이터 표출
      data.forEach((item, dataIndex) => {
        let dataColumnIndex = 1;
        excelHeaders.forEach((header) => {
          if (header.children) {
            header.children.forEach((child) => {
              const cell = worksheet.getCell(dataIndex + 4, dataColumnIndex);
              cell.value = item[child.key];
              if (typeof cell.value === 'number') {
                cell.numFmt = '#,##0'; // 숫자 포맷 설정
              }
              cell.alignment = { horizontal: 'center', vertical: 'middle' };
              cell.border = {
                top: { style: 'thin' },
                left: { style: 'thin' },
                bottom: { style: 'thin' },
                right: { style: 'thin' },
              };
              dataColumnIndex++;
            });
          } else {
            const cell = worksheet.getCell(dataIndex + 4, dataColumnIndex);
            cell.value = item[header.key];
            if (typeof cell.value === 'number') {
              cell.numFmt = '#,##0'; // 숫자 포맷 설정
            }
            cell.alignment = { horizontal: 'center', vertical: 'middle' };
            cell.border = {
              top: { style: 'thin' },
              left: { style: 'thin' },
              bottom: { style: 'thin' },
              right: { style: 'thin' },
            };
            dataColumnIndex++;
          }
        });
      });
    }

    // 맨 마지막 줄 데이터 표출(건수, 합계 등)
    if (addedRow != null) {
      const row = worksheet.rowCount + 1;
      addedRow.forEach((value, idx) => {
        const col = idx + 1;
        const cell = worksheet.getCell(row, col);
        cell.value = value;
        cell.font = { bold: true };
        cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'a0a0a0' } };
        cell.alignment = { horizontal: 'center', vertical: 'middle' };
        cell.border = {
          top: { style: 'thin' },
          left: { style: 'thin' },
          bottom: { style: 'thin' },
          right: { style: 'thin' },
        };
      });

      worksheet.mergeCells(row, 1, row, colCount);

      for (let idx = 0; idx < colCount; idx++) {
        if (idx > addedRow.length) {
          const cell = worksheet.getCell(row, idx);
          cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'f0f0f0 ' } };
          cell.alignment = { horizontal: 'left', vertical: 'middle' };
          cell.border = {
            top: { style: 'thin' },
            left: { style: 'thin' },
            bottom: { style: 'thin' },
            right: { style: 'thin' },
          };
        }
      }
    }

    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    saveAs(blob, fileName);
    showMessage(t('SUCCESS_EXCEL'), 'success');
  } catch (error) {
    alert(t('FUNCTION_ERROR_EXCEL'));
  }
};
