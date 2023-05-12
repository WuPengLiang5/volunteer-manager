package com.wpl.volunteer.controller.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dto.QueryActivityDTO;
import com.wpl.volunteer.dto.QueryNewsDTO;
import com.wpl.volunteer.dto.QuerySignUpDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.Columns;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.entity.SignUp;
import com.wpl.volunteer.service.*;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1.0/out")
public class PortalController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private IColumnsService columnService;

    @RequestMapping("/activity/listActivities")
    public ResultCountVo listActivities(@RequestBody QueryActivityDTO queryActivityDTO){
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        if(queryActivityDTO.getTitle()!=null && !"".equals(queryActivityDTO.getTitle()))
            wrapper.like("title",queryActivityDTO.getTitle());
        if(queryActivityDTO.getIntroduction()!=null && !"".equals(queryActivityDTO.getIntroduction()))
            wrapper.like("introduction",queryActivityDTO.getIntroduction());
        if(queryActivityDTO.getLocation()!=null && !"".equals(queryActivityDTO.getLocation()))
            wrapper.like("location",queryActivityDTO.getLocation());
        if(queryActivityDTO.getRegisterMax()!=null && !"".equals(queryActivityDTO.getRegisterMax()))
            wrapper.like("register_max",queryActivityDTO.getRegisterMax());
        if(queryActivityDTO.getState() != null){
            int state = queryActivityDTO.getState();
            if (state >= 10){
                queryActivityDTO.setState(queryActivityDTO.getState() / 10);
                wrapper.eq("state",queryActivityDTO.getState());
                if (state == 10){
                    wrapper.apply("UNIX_TIMESTAMP(now()) < UNIX_TIMESTAMP(registration_begin)");
                }
                if (state == 11){
                    wrapper.apply("UNIX_TIMESTAMP(registration_begin) <= UNIX_TIMESTAMP(now())" +
                            "and UNIX_TIMESTAMP(now()) <= UNIX_TIMESTAMP(registration_end)");
                }
                if (state == 12){
                    wrapper.apply("UNIX_TIMESTAMP(registration_end) < UNIX_TIMESTAMP(now())" +
                            "and UNIX_TIMESTAMP(now()) < UNIX_TIMESTAMP(end_time)");
                }
            }else{
                wrapper.eq("state",queryActivityDTO.getState());
            }
        }else{
            // 获取不是未发布和未审核的活动
            wrapper.notIn("state", Collections.singletonList(new int[]{-1, 0}));
        }

        wrapper.orderByDesc("create_time");

        IPage<Activity> page = new Page<>(queryActivityDTO.getPage(), queryActivityDTO.getLimit());
        activityService.page(page, wrapper);

        List<Activity> activities = page.getRecords();
        LocalDateTime localDateTime = LocalDateTime.now();
        // 设置活动的状态 运行中（招募待启动、招募中、招募已结束）、已结束
        for (Activity activity:activities){
            if (localDateTime.compareTo(activity.getRegistrationBegin()) < 0){
                activity.setState(10);
            }
            if (localDateTime.compareTo(activity.getRegistrationBegin()) > 0
                && localDateTime.compareTo(activity.getRegistrationEnd()) < 0){
                activity.setState(11);
            }
            if (localDateTime.compareTo(activity.getRegistrationEnd()) > 0
                && localDateTime.compareTo(activity.getEndTime()) < 0){
                activity.setState(12);
            }
        }

        return new ResultCountVo(ResultVo.success("获取成功",activities), page.getTotal());
    }

    @GetMapping("/activity/getActivityById")
    public ResultVo getActivityById(@RequestParam Integer id){
        Activity activity=activityService.getActivityById(id);
        if (activity==null){
            return ResultVo.error("活动不存在");
        }
        return ResultVo.success("获取成功",activity);
    }

    @GetMapping("/news/getNewsDetails")
    public ResultVo getNewsDetails(Integer id){
        News news = newsService.getNewsById(id);
        if (news == null){
            return ResultVo.error("新闻不存在");
        }
        return ResultVo.success("获取成功",news);
    }

    @PostMapping("/news/getNewsByType")
    public ResultVo getNewsByType(@RequestBody QueryNewsDTO queryNewsDTO){
        return ResultVo.success("获取成功",newsService.getNewsByType(queryNewsDTO));
    }

    @GetMapping("/signup/getSignUpByAIdAndUId")
    public ResultVo getSignUpByAIdAndUId(@RequestParam Integer userId, @RequestParam Integer activityId){
        SignUp SignUp = signUpService.getSignUpByAIdAndUId(userId,activityId);
        return ResultVo.success("获取成功",SignUp);
    }

    @RequestMapping("/signup/getSignUpByActivityId")
    public ResultVo getSignUpByActivityId(@RequestBody QuerySignUpDTO querySignUpDTO){
        return signUpService.getSignUpByActivityId(querySignUpDTO);
    }

    @GetMapping("/volunteer/getVolunteerMap")
    public ResultVo getVolunteerMap(){
        return new ResultCountVo(ResultVo.success("获取成功",volunteerService.listVolunteerMap()),volunteerService.selectCount());
    }

    @GetMapping("/getIndexesNewsList")
    public ResultVo getIndexesNewsList(){
        List<Columns> columns = columnService.listWebColumns();
        columns.removeIf(columns1 -> columns1.getNewsSort() == null);
        Collections.sort(columns, new Comparator<Columns>() {
            @Override
            public int compare(Columns o1, Columns o2) {
                return o1.getNewsSort().compareTo(o2.getNewsSort());
            }
        });

        List<Map<String,Object>> result = new ArrayList<>();
        for (Columns columns1:columns){
            List<Columns> child = columnService.getColumnsByPid(columns1.getId());
            Map<String,Object> resultMap = JSONObject.parseObject(JSON.toJSONString(columns1));
            if (child.size() == 0){
                QueryNewsDTO queryNewsDTO = new QueryNewsDTO();
                queryNewsDTO.setPage(1);
                queryNewsDTO.setLimit(6);
                queryNewsDTO.setNewsType(columns1.getId());
                resultMap.put("newsList",newsService.getNewsByType(queryNewsDTO).get("newsList"));
                resultMap.put("total",newsService.getNewsByType(queryNewsDTO).get("total"));
            }else{
                List<Map<String,Object>> menuList = new ArrayList<>();
                for (Columns childColumn:child){
                    QueryNewsDTO queryNewsDTO = new QueryNewsDTO();
                    queryNewsDTO.setPage(1);
                    queryNewsDTO.setLimit(4);
                    queryNewsDTO.setNewsType(childColumn.getPid() + "," + childColumn.getId());
                    Map<String,Object> newsMap = newsService.getNewsByType(queryNewsDTO);

                    Map<String,Object> map1 = JSONObject.parseObject(JSON.toJSONString(childColumn));
                    map1.put("newsList",newsMap.get("newsList"));
                    map1.put("total",newsMap.get("total"));
                    menuList.add(map1);
                }
                resultMap.put("menuList",menuList);
            }

            result.add(resultMap);
        }

        return ResultVo.success("获取成功",result);
    }

    @RequestMapping("/columns/columnsById")
    public ResultVo columnsById(@RequestBody QueryNewsDTO queryNewsDTO){
        List<Columns> columns = columnService.getColumnsByPid(queryNewsDTO.getNewsType());
        Columns parentColumn = columnService.getById(queryNewsDTO.getNewsType());
        if (parentColumn == null){
            return ResultVo.error("获取失败");
        }

        if (columns.size() == 0){
            Map<String,Object> newsMap = newsService.getNewsByType(queryNewsDTO);

            Map<String,Object> resultMap = JSONObject.parseObject(JSON.toJSONString(parentColumn));
            resultMap.put("newsList",newsMap.get("newsList"));
            resultMap.put("total",newsMap.get("total"));

            return ResultVo.success("获取成功",resultMap);
        }else{
            Map<String,Object> resultMap = JSONObject.parseObject(JSON.toJSONString(parentColumn));
            List<Map<String,Object>> menuList = new ArrayList<>();
            for (Columns columns1:columns){
                queryNewsDTO.setNewsType(columns1.getPid() + "," + columns1.getId());
                Map<String,Object> newsMap = newsService.getNewsByType(queryNewsDTO);

                Map<String,Object> map1 = JSONObject.parseObject(JSON.toJSONString(columns1));
                map1.put("newsList",newsMap.get("newsList"));
                map1.put("total",newsMap.get("total"));
                menuList.add(map1);
            }
            resultMap.put("menuList",menuList);
            return ResultVo.success("获取成功",resultMap);
        }
    }

    @RequestMapping("/columns/newsById")
    public ResultVo newsById(@RequestBody QueryNewsDTO queryNewsDTO){
        Columns columns = columnService.getById(queryNewsDTO.getNewsType());
        if (columns == null){
            return ResultVo.error("获取失败");
        }

        Map<String,Object> resultMap = JSONObject.parseObject(JSON.toJSONString(columns));
        queryNewsDTO.setNewsType(columns.getPid() + ',' + columns.getId());
        resultMap.put("title",columns.getTitle());
        resultMap.put("newsList",newsService.getNewsByType(queryNewsDTO).get("newsList"));
        resultMap.put("total",newsService.getNewsByType(queryNewsDTO).get("total"));

        return ResultVo.success("获取成功",resultMap);
    }

    @RequestMapping("/columns/listWebColumns")
    public ResultVo listWebColumns(){
        return ResultVo.success("获取成功",columnService.listWebColumns());
    }
}
