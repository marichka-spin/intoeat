package net.lviv.intoeat.services;

import net.lviv.intoeat.models.User;

import java.util.List;

public interface UserService {

    public User getUserById(Integer id);
    public User getUserByUsername(String username);
    public List<User> getUsers();
    public User saveUser(User user);
    public void deleteUser(Integer id);

}
