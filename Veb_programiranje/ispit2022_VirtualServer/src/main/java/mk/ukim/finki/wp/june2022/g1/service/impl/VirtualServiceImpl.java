package mk.ukim.finki.wp.june2022.g1.service.impl;
import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VirtualServiceImpl implements VirtualServerService {

    private final UserRepository userRepository;
    private final VirtualServerRepository virtualServerRepository;

    public VirtualServiceImpl(UserRepository userRepository, VirtualServerRepository virtualServerRepository) {
        this.userRepository = userRepository;
        this.virtualServerRepository = virtualServerRepository;
    }

    @Override
    public List<VirtualServer> listAll() {
        return virtualServerRepository.findAll();
    }

    @Override
    public VirtualServer findById(Long id) {
        return virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException :: new);
    }

    @Override
    public VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate) {
        List<User> userList = userRepository.findAllById(owners);
        return virtualServerRepository.save(new VirtualServer(name,ipAddress,osType, userList, launchDate));
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        List<User> userList = userRepository.findAllById(owners);
        VirtualServer virtualServer = virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException :: new);
        virtualServer.setInstanceName(name);
        virtualServer.setIpAddress(ipAddress);
        virtualServer.setOwners(userList);
        virtualServer.setOSType(osType);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer virtualServer = virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException :: new);
        virtualServerRepository.delete(virtualServer);
        return virtualServer;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        return null;
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        return null;
    }
}
