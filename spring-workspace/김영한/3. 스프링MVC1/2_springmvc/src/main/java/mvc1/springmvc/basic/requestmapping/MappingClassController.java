package mvc1.springmvc.basic.requestmapping;

// REST ful 한 API 만들어서 매핑 테스트

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
    /*
        회원 목록 조회: GET /users
        회원 등록: POST /users
        회원 조회: GET /users/{userId}
        회원 수정: PATCH /users/{userId}
        회원 삭제: DELETE /users/{userId}
    */

    // Post Man에서 HTTP 메서드 변경 해 가며 테스트

    // http://localhost:8082/mapping/users
    @GetMapping
    public String user() {
        return "GET 매핑 all users";
    } // end user()

    // http://localhost:8082/mapping/users
    @PostMapping
    public String addUser() {
        return "POST 매핑 user";
    } // end addUser()

    // http://localhost:8082/mapping/users/abcd
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "GET 매핑 userId = " + userId;
    } // end findUser()

    // http://localhost:8082/mapping/users/abcd
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "UPDATE 매핑 userId = " + userId;
    } // end updateUser()

    // http://localhost:8082/mapping/users/abcd
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "DELETE 매핑 userId = " + userId;
    } // end deleteUser()


    @PutMapping("/{userId}")
    public String putUser(@PathVariable String userId) {
        return "PUT 매핑 userId = " + userId;
    }
}