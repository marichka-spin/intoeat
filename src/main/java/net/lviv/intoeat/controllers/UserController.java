package net.lviv.intoeat.controllers;

import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.services.UserService;
import net.lviv.intoeat.utils.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    @ResponseBody
    public User getUser(@RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "username", required = false) String username) {
        if (id != null) {
            return userService.getUserById(id);
        } else if (!StringUtils.isEmpty(username)) {
            return userService.getUserByUsername(username);
        } else {
            throw new InvalidInputParametersException("The given id must not be null!");
        }

    }

    @RequestMapping("/user/all")
    @ResponseBody
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        ResponseInfo responseInfo = new ResponseInfo(savedUser.getId());
        return responseInfo;
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseInfo removeUser(@RequestParam(value = "id", required = true) Integer id) {
        userService.deleteUser(id);
        ResponseInfo responseInfo = new ResponseInfo(id);
        return responseInfo;
    }

}
