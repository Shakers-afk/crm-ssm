package com.shantouxzk.crm.workbench.service.impl;

import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.workbench.dao.CustomerDao;
import com.shantouxzk.crm.workbench.dao.TranDao;
import com.shantouxzk.crm.workbench.dao.TranHistoryDao;
import com.shantouxzk.crm.workbench.domain.Customer;
import com.shantouxzk.crm.workbench.domain.Tran;
import com.shantouxzk.crm.workbench.domain.TranHistory;
import com.shantouxzk.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    private TranDao tranDao;
    private TranHistoryDao tranHistoryDao;
    private CustomerDao customerDao;

    @Autowired
    public void setTranDao(TranDao tranDao) {
        this.tranDao = tranDao;
    }

    @Autowired
    public void setTranHistoryDao(TranHistoryDao tranHistoryDao) {
        this.tranHistoryDao = tranHistoryDao;
    }

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Transactional
    @Override
    public Boolean save(Tran tran, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setName(customerName);
            customer.setOwner(tran.getOwner());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setContactSummary(tran.getContactSummary());

            flag = customerDao.save(customer) == 1;
        }
        //添加交易
        tran.setCustomerId(customer.getId());
        flag = tranDao.save(tran) == 1 && flag;

        //添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setTranId(tran.getId());
        flag = tranHistoryDao.save(tranHistory) == 1 && flag;

        return flag;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.detail(id);
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        return tranHistoryDao.getHistoryListByTranId(tranId);
    }

    @Transactional
    @Override
    public Boolean changeStage(Tran tran) {
        boolean flag;
        flag = tranDao.changeStage(tran) == 1;

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        flag = tranHistoryDao.save(tranHistory) == 1 && flag;
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        Map<String, Object> map = new HashMap<>();
        int total = tranDao.getTotal();
        List<Map<String,Object>> dataList = tranDao.getCharts();

        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
