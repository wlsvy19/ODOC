// api url
var url = 'https://jsonplaceholder.typicode.com/users/1';

// dom
var username = document.querySelector('#username');
var email = document.querySelector('#email');
var address = document.querySelector('#address');

// user data
var user = {};

/** 타입정의
 * @typedef {object} Address
 * @property {string} name
 * @property {string} email
 * @property {string} address
 * 
 */

/** 타입정의
 * @typedef {object} User
 * @property {string} name
 * @property {string} email
 * @property {string} address
 */

/**
 * 
 * @returns {Promise<User>}
 */

function fetchUser() {
  return axios.get(url);
}

fetchUser().then(function (response) {
  response.address
})

function startApp() {
  fetchUser()
    .then(function (response) {
      //console.log(response);
      user = response.data;
      // TODO: 이름, 이메일, 주소 표시하기
      console.log(user)
      username.innerText = user.name;
      email.innerText = user.email;
      address.innerText = user.address.street
    })
    .catch(function (error) {
      console.log(error);
    });
}



startApp();