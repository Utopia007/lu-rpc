package org.example.provider;

import org.example.common.service.UserService;
import org.example.lurpc.bootstrap.ProviderBootstrap;
import org.example.lurpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/16 15:26
 * @description:
 */
public class ProviderForBootstrap {

    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        ProviderBootstrap.init(serviceRegisterInfoList);
    }

}
