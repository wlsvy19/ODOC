// 내가 만든 타입들을 모둘화 하여 export 하기

// 방법1: 각각 export 하기
export interface PhoneNumberDictionary {
  [phone: string]: {
    num: number;
  };
}

interface Contact {
  name: string;
  address: string;
  phones: PhoneNumberDictionary;
}

enum PhoneType {
  Home = "home",
  Office = "office",
  Studio = "studio",
}

// 방법2: 한번에 export 하기
export { Contact, PhoneType };
