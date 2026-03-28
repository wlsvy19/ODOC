interface ContactRingProps {
  ringNumber: string;
  onSearch: () => void;
}

export function ContactRing({ ringNumber, onSearch }: ContactRingProps) {
  return (
    <div className="flex items-center gap-3">
      <div className="border border-gray-300 rounded px-6 py-2 bg-white min-w-[120px] text-center">
        <span className="text-sm font-medium text-gray-900">{ringNumber}</span>
      </div>
      <div className="flex items-center gap-2">
        <span className="text-sm text-gray-700">▶</span>
        <button
          onClick={onSearch}
          className="px-4 py-1.5 bg-white border border-gray-300 rounded text-sm text-gray-900 hover:bg-gray-50"
        >
          검색
        </button>
        <span className="text-sm text-gray-700">▶</span>
      </div>
      <button className="px-6 py-2 bg-gray-700 text-white rounded text-sm hover:bg-gray-800">
        원격관리자 DB 시현
      </button>
    </div>
  );
}

export function TechnicalContact() {
  return (
    <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-200">
      <div className="flex items-center gap-2 mb-4">
        <div className="w-6 h-6 bg-blue-900 rounded-full flex items-center justify-center text-white text-xs font-bold">
          11
        </div>
        <h3 className="text-sm font-semibold text-gray-900">관건통합관리 기술연락망</h3>
      </div>
      <div className="flex items-center justify-between gap-4">
        <ContactRing ringNumber="1Ring" onSearch={() => {}} />
        <ContactRing ringNumber="2Ring" onSearch={() => {}} />
        <ContactRing ringNumber="3Ring" onSearch={() => {}} />
      </div>
    </div>
  );
}
