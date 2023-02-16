let arr = [
  { gender: 'male', name: 'john' },
  { gender: 'female', name: 'sara' },
  { gender: 'male', name: 'pops' }
]

// 특정 아이템 추출하는 API
let filtered = arr.filter(function (item) {
  if (item.gender === 'female') {
    return item;
  }
})

console.log(filtered);