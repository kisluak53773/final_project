package com.example.webproject.service.impl;

import com.example.webproject.controller.command.Pagination;
import com.example.webproject.dao.ServiceDAO;
import com.example.webproject.dao.entity.Service;
import com.example.webproject.dao.entity.User;
import com.example.webproject.dao.impl.DAOAllocator;
import com.example.webproject.dao.impl.ServiceDAOImpl;
import com.example.webproject.exception.DAOException;
import com.example.webproject.exception.ServiceException;
import com.example.webproject.service.ServiceService;
import com.example.webproject.service.util.PaginationHelper;
import com.example.webproject.service.validator.ServiceFilterValidator;
import com.example.webproject.service.validator.impl.ServiceFilterValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.webproject.controller.RequestAttribute.*;

public class ServiceServiceImpl implements ServiceService {
    private static final ServiceServiceImpl instance = new ServiceServiceImpl();
    private static final Logger logger = LogManager.getLogger(ServiceServiceImpl.class);

    private static final int PAGINATED_PAGE_ELEMENTS = 5;

    private ServiceServiceImpl() {
    }

    public static ServiceServiceImpl getInstance() {
        return instance;
    }
    @Override
    public List<Service> findAll() throws ServiceException {
        try {
            ServiceDAO serviceDAO= DAOAllocator.getServiceDao();
            List<Service> announcements = serviceDAO.findAll();
            return announcements;

        } catch (DAOException e) {
            logger.error("Can't load all announcements.", e);
            throw new ServiceException("Can't load all announcements.", e);
        }
    }

    @Override
    public Optional<Service> save(Service announcement) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public Pagination<Service> findServices(Map<String, List<String>> parameterMap) throws ServiceException {
        try {
            ServiceFilterValidator filterValidator = ServiceFilterValidatorImpl.getInstance();
            Map<String, List<String>> validatedParameters = filterValidator.validateParameterMap(parameterMap);
            ServiceDAOImpl serviceDAO = DAOAllocator.getServiceDao();
            List<Service> allServices = serviceDAO.findAll();
            List<Service> filteredServices = filterData(allServices, validatedParameters);

            PaginationHelper paginationHelper = PaginationHelper.getInstance();
            List<String> pages = parameterMap.get(PAGE);
            int page = paginationHelper.findPage(pages);

            Pagination<Service> pagination = paginationHelper.getPage(filteredServices, page, PAGINATED_PAGE_ELEMENTS);
            pagination.setRequestAttributes(validatedParameters);
            return pagination;

        } catch (DAOException e) {
            logger.error("Can't find paginated page", e);
            throw new ServiceException("Can't find paginated page", e);
        }
    }

    @Override
    public Pagination<Service> findServices(Map<String, List<String>> parameterMap, User user) throws ServiceException {
        return null;
    }

    @Override
    public Optional<Service> findById(String id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public Optional<Service> updateService(Map<String, List<String>> parameterMap, User user) throws ServiceException {
        return Optional.empty();
    }

    private List<Service> filterData(List<Service> services, Map<String, List<String>> parameterMap) {
        List<Integer> prices = toIntList(parameterMap.get(PRICE));
        List<String> serviceNames=parameterMap.get(SERVICE_NAME);
        List<String> descriptions = parameterMap.get(DESCRIPTION);
        List<String> images = parameterMap.get(IMAGE);
        Predicate<Service> pricePredicate = service -> prices.contains(Integer.parseInt(String.valueOf(service.getPrice())));
        Predicate<Service> serviceNamePredicate = service -> serviceNames.contains(service.getServiceName());
        Predicate<Service> descriptionPredicate=service ->descriptions.contains(service.getServiceDescription());
        Predicate<Service> imagePredicate=service -> images.contains(service.getServiceImage());
        List<Service> filteredAnnouncements = services.stream()
                .filter(!prices.isEmpty() ? pricePredicate : a -> true)
                .filter(!serviceNames.isEmpty() ? serviceNamePredicate : a -> true)
                .filter(!descriptions.isEmpty() ? descriptionPredicate : a -> true)
                .filter(!images.isEmpty() ? imagePredicate : a -> true)
                .collect(Collectors.toList());
        return filteredAnnouncements;
    }

    private List<Integer> toIntList(List<String> source) {
        return source.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
