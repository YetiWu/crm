package com.yetiwu.crm.workbench.service.impl;

import com.yetiwu.crm.settings.dao.DicTypeDao;
import com.yetiwu.crm.settings.dao.DicValueDao;
import com.yetiwu.crm.settings.dao.UserDao;
import com.yetiwu.crm.settings.domain.DicType;
import com.yetiwu.crm.settings.domain.DicValue;
import com.yetiwu.crm.settings.domain.User;
import com.yetiwu.crm.utils.DateTimeUtil;
import com.yetiwu.crm.utils.SqlSessionUtil;
import com.yetiwu.crm.utils.UUIDUtil;
import com.yetiwu.crm.vo.PaginationVo;
import com.yetiwu.crm.workbench.dao.*;
import com.yetiwu.crm.workbench.domain.*;
import com.yetiwu.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    // 线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    // 客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    // 联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        // 获取dictype表中的所有信息
        List<DicType> typeList = dicTypeDao.getDicType();

        // 遍历集合
        for (DicType dicType : typeList) {
            // 获取每一个dicType对象的typecode
            String code = dicType.getCode();
            // 根据typecode去查找相对应的dicvalue
            List<DicValue> valueList = dicValueDao.getValueByCode(code);
            map.put(code, valueList);
        }
        return map;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = true;
        int count = clueDao.saveClue(clue);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo pageList(Map<String, Object> map) {

        List<Clue> clueList = clueDao.getClue(map);

        int total = clueDao.getTotalCount(map);

        PaginationVo vo = new PaginationVo();
        vo.setTotal(total);
        vo.setPageList(clueList);

        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(Map<String, Object> map) {
        boolean flag = true;
        String cid = (String) map.get("cid");
        String aid[] = (String[]) map.get("aid");
        for (String id : aid) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setClueId(cid);
            car.setActivityId(id);
            car.setId(UUIDUtil.getUUID());
            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        // 生成创建时间
        String createTime = DateTimeUtil.getSysTime();

        // (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getClueById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if(customer == null){
            // 新建客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(createTime);
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(clue.getCreateBy());
            customer.setDescription(clue.getDescription());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
        }
        // 添加客户信息
        int count = customerDao.save(customer);
        if(count != 1){
            flag = false;
        }

        // (3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setCreateBy(clue.getCreateBy());
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setOwner(clue.getOwner());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setJob(clue.getJob());
        contacts.setNextContactTime(clue.getNextContactTime());
        int count1 = contactsDao.save(contacts);
        if(count1 != 1){
            flag = false;
        }
        // (4) 线索备注转换到客户备注以及联系人备注
        // 查询线索备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getClueRemarkByClueId(clueId);
        for(ClueRemark clueRemark : clueRemarkList){
            // 将线索备注转换到客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setEditFlag("0");
            int count2 = customerRemarkDao.save(customerRemark);
            if(count2 != 1){
                flag = false;
            }

            // 将线索备注转换到联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setEditFlag("0");
            int count3 = contactsRemarkDao.save(contactsRemark);
            if(count3 != 1){
                flag = false;
            }
        }
        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        // 查询线索和市场活动中指定线索id对应的市场活动id
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            int count4 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count4 != 1){
                flag = false;
            }
        }
        // (6) 如果有创建交易需求，创建一条交易
        if(tran != null){
            // tran:money,name,expectedDate,stage,activityId
            tran.setId(UUIDUtil.getUUID());
            tran.setContactsId(contacts.getId());
            tran.setOwner(clue.getOwner());
            tran.setSource(clue.getSource());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setCreateTime(createTime);
            tran.setCreateBy(createBy);
            tran.setContactSummary(clue.getContactSummary());
            // 向交易表中添加记录
            int count5 = tranDao.save(tran);
            if(count5 != 1){
                flag = false;
            }

            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            int count6 = tranHistoryDao.save(tranHistory);
            if(count6 != 1){
                flag = false;
            }
        }

        // (8) 删除线索备注
        for(ClueRemark clueRemark : clueRemarkList){
            int count7 = clueRemarkDao.deleteRemarkByClueId(clueRemark.getClueId());
            if(count7 != 1){
                flag = false;
            }
        }
        // (9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            int count8 = clueActivityRelationDao.unbund(clueActivityRelation.getId());
            if(count8 != 1){
                flag = false;
            }
        }
        // (10) 删除线索
        int count9 = clueDao.deleteClueById(clueId);
        if(count9 != 1){
            flag = false;
        }

        return flag;
    }
}
