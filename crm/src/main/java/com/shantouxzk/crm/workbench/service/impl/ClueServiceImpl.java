package com.shantouxzk.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shantouxzk.crm.utils.DateTimeUtil;
import com.shantouxzk.crm.utils.UUIDUtil;
import com.shantouxzk.crm.vo.PaginationVo;
import com.shantouxzk.crm.workbench.dao.*;
import com.shantouxzk.crm.workbench.domain.*;
import com.shantouxzk.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Shakers
 */
@Service
public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao;
    private ClueRemarkDao clueRemarkDao;
    private CustomerDao customerDao;
    private CustomerRemarkDao customerRemarkDao;
    private ClueActivityRelationDao clueActivityRelationDao;
    private ContactsDao contactsDao;
    private ContactsRemarkDao contactsRemarkDao;
    private ContactsActivityRelationDao contactsActivityRelationDao;
    private TranDao tranDao;
    private TranHistoryDao tranHistoryDao;

    @Autowired
    public void setTranDao(TranDao tranDao) {
        this.tranDao = tranDao;
    }

    @Autowired
    public void setTranHistoryDao(TranHistoryDao tranHistoryDao) {
        this.tranHistoryDao = tranHistoryDao;
    }

    @Autowired
    public void setContactsActivityRelationDao(ContactsActivityRelationDao contactsActivityRelationDao) {
        this.contactsActivityRelationDao = contactsActivityRelationDao;
    }

    @Autowired
    public void setContactsDao(ContactsDao contactsDao) {
        this.contactsDao = contactsDao;
    }

    @Autowired
    public void setContactsRemarkDao(ContactsRemarkDao contactsRemarkDao) {
        this.contactsRemarkDao = contactsRemarkDao;
    }

    @Autowired
    public void setClueDao(ClueDao clueDao) {
        this.clueDao = clueDao;
    }

    @Autowired
    public void setClueRemarkDao(ClueRemarkDao clueRemarkDao) {
        this.clueRemarkDao = clueRemarkDao;
    }

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Autowired
    public void setCustomerRemarkDao(CustomerRemarkDao customerRemarkDao) {
        this.customerRemarkDao = customerRemarkDao;
    }

    @Autowired
    public void setClueActivityRelationDao(ClueActivityRelationDao clueActivityRelationDao) {
        this.clueActivityRelationDao = clueActivityRelationDao;
    }

    @Override
    public Boolean save(Clue clue) {
        int count = clueDao.save(clue);
        return count == 1;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public Boolean unbund(String id) {
        int count = clueActivityRelationDao.unbund(id);
        return count == 1;
    }

    @Transactional
    @Override
    public Boolean bund(String clueId, String[] activityIds) {
        boolean flag = true;
        ClueActivityRelation car = new ClueActivityRelation();
        for (String activityId : activityIds) {
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(activityId);
            car.setClueId(clueId);

            int count = clueActivityRelationDao.bund(car);
            if (count!=1){
                flag = false;
            }
        }
        return flag;
    }

    @Transactional
    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        //1.根据线索id获取线索对象，(线索对象当中封装了线索的信息)
        Clue clue = clueDao.getById(clueId);

        String company = clue.getCompany();
        String createTime = DateTimeUtil.getSysTime();
        //2.通过线索对象提取客户信息，当该客户不存在的时候，新建客户(根据公司的名称精确匹配，判断该客户是否存在)
        Customer customer = customerDao.getCustomerByName(company);
        //如果customer为空，说明没有这个客户，需要新建一个
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setAddress(clue.getAddress());
            customer.setDescription(clue.getDescription());
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            customer.setContactSummary(clue.getContactSummary());

            flag = customerDao.save(customer)==1;
        }
        //经过第二步处理后，客户的信息已经拥有了，将来在处理其他表的时候，如果要使用到客户的id
        //直接使用customer.getId() 拿id就可以了

        //3.通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setEmail(clue.getEmail());
        contacts.setJob(clue.getJob());
        contacts.setDescription(clue.getDescription());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setMphone(clue.getMphone());
        contacts.setCustomerId(customer.getId());
        contacts.setAddress(clue.getAddress());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        flag = contactsDao.save(contacts) == 1 && flag;

        //4.线索备注转换到客户备注以及联系人备注
        //根据clueId查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark:
                clueRemarkList) {
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            flag = customerRemarkDao.save(customerRemark) == 1 && flag;

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            flag = contactsRemarkDao.save(contactsRemark) == 1 && flag;

        }
        //5.“线索市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            flag = contactsActivityRelationDao.save(contactsActivityRelation) == 1 && flag;
        }

        if (!"".equals(tran.getId())){
             /*
                clue对象在controller里面已经封装好的信息如下:
                    id,money,name,expectedDate,stage,activityId,createBy,createTime
                接下来可以通过第一步生成的clue对象，取出一些信息，继续完善对tran对象的封装.根据用户需求来。
             */
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            flag = tranDao.save(tran) == 1 && flag;
            //7.如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setTranId(tran.getId());
            flag = tranHistoryDao.save(tranHistory) == 1 && flag;
        }

        //8.删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            flag = clueRemarkDao.delete(clueRemark) == 1 && flag;
        }

        //9.删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            flag = clueActivityRelationDao.delete(clueActivityRelation) == 1 && flag;
        }

        //10.删除线索
        flag = clueDao.delete(clueId) == 1 && flag;

        return flag;
    }




}
