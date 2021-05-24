package com.shantouxzk.crm.workbench.service.impl;

import com.shantouxzk.crm.workbench.dao.CustomerDao;
import com.shantouxzk.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }
}
