package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.QueryNewsDTO;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.NewsService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Log(module = "新闻管理",businessType = BusinessType.OTHER)
    @RequestMapping("/listNews")
    public ResultCountVo listNews(@RequestBody QueryNewsDTO queryNewsDTO){
        return newsService.listNews(queryNewsDTO);
    }

    @GetMapping("/getNewsById")
    public ResultVo getNewsById(Integer id){
        News news = newsService.getNewsById(id);
        if (news == null){
            return ResultVo.error("新闻不存在");
        }
        return ResultVo.success("获取成功",news);
    }

    @Log(module = "新闻管理",businessType = BusinessType.INSERT)
    @PostMapping("/saveNews")
    public ResultVo saveNews(@RequestBody News news){
        int result = newsService.saveNews(news);
        if (result != 1){
            return ResultVo.error("添加新闻失败");
        }
        return ResultVo.success("成功添加新闻");
    }

    @Log(module = "新闻管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delNews")
    public ResultVo removeNews(@RequestParam Integer newsId){
        int result = newsService.removeNews(newsId);
        if (result != 1){
            return ResultVo.error("删除新闻失败");
        }
        return ResultVo.success("成功删除新闻");
    }

    @Log(module = "新闻管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateNews")
    public ResultVo updateNews(@RequestBody News news){
        int result = newsService.updateNews(news);
        if (result != 1){
            return ResultVo.error("更新新闻失败");
        }
        return ResultVo.success("成功更新新闻");
    }

    @Log(module = "新闻管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delBatchNews")
    public ResultVo removeNews(Integer[] ids){
        int result = newsService.removeNews(ids);
        if (result < 0){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }

    @Log(module = "新闻管理",businessType = BusinessType.OTHER)
    @GetMapping("/getNewsCount")
    public ResultVo getNewsCount(){
        return ResultVo.success("获取成功",newsService.selectCount());
    }
}
