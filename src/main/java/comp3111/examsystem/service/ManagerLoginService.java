package comp3111.examsystem.service;

import comp3111.examsystem.data.DataManager;
import comp3111.examsystem.entity.Manager;

import java.util.List;

public class ManagerLoginService {
    private final DataManager dataManager;

    // 构造函数：通过 DataManager 获取管理员数据
    public ManagerLoginService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 验证管理员登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果用户名和密码匹配，返回 true；否则返回 false
     */
    public boolean validate(String username, String password) {
        // 获取所有管理员
        List<Manager> managers = dataManager.getManagers();

        for (Manager manager : managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return true; // 验证成功
            }
        }

        return false; // 验证失败
    }
}
