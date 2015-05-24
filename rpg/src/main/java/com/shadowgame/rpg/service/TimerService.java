package com.shadowgame.rpg.service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xgame.core.util.Config;
import xgame.core.util.Service;
import xgame.core.util.ThreadNameFactory;

import com.shadowgame.rpg.core.AppConfig;
import com.shadowgame.rpg.core.AppException;

public class TimerService implements Service {
	private static final Logger log = LoggerFactory.getLogger(TimerService.class);
	public ScheduledThreadPoolExecutor jdkScheduler; // 定时器
	public Scheduler quartzScheduler;

	@Override
	public void start() throws Exception {
		jdkScheduler = new ScheduledThreadPoolExecutor(
				AppConfig.JDK_SCHEDULE_CORE_POOL_SIZE, new ThreadNameFactory("jdk scheduler"));
		
		// 读取默认配置文件 增加插件
		Properties ps = new Config("org/quartz/quartz.properties").getProperties();
		ps.setProperty("org.quartz.threadPool.threadCount", AppConfig.QUARTZ_SCHEDULE_CORE_POOL_SIZE); // 线程池大小
		ps.setProperty("org.quartz.plugin.jobInitializer.class", "org.quartz.plugins.xml.JobInitializationPlugin");
		ps.setProperty("org.quartz.plugin.jobInitializer.scanInterval", "0"); //relaod配置文件间隔时间秒，0不扫描
		// ps.setProperty("org.quartz.plugin.jobInitializer.overWriteExistingJobs", "true"); //覆盖配置文件中同名JobDetail
		quartzScheduler = new StdSchedulerFactory(ps).getScheduler();
		quartzScheduler.start();
	}

	@Override
	public void stop() throws Exception {
		jdkScheduler.shutdownNow();
		quartzScheduler.shutdown(true);
	}

	/**
	 * 安排quartz任务
	 * @param jobName 任务名
	 * @param cron cron表达式
	 * @param jobClass 任务类
	 * @param jobData 任务数据map
	 * 
	 *            CronTrigger配置完整格式为： [秒] [分] [小时] [日] [月] [周] [年]
	 * 
	 *            序号 说明 是否必填 允许填写的值 允许的通配符 1 秒 是 0-59 , - * / 2 分 是 0-59 , - * /
	 *            3 小时 是 0-23 , - * / 4 日 是 1-31 , - * ? / L W 5 月 是 1-12 or
	 *            JAN-DEC , - * / 6 周 是 1-7 or SUN-SAT , - * ? / L # 7 年 否 empty
	 *            或 1970-2099 , - * /
	 * 
	 *            通配符说明: 表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。 ?
	 *            表示不指定值。使用的场景为不需要关心当前设置这个字段的值
	 *            。例如:要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为"?" 具体设置为 0 0 0 10 *
	 *            ? - 表示区间。例如 在小时上设置 "10-12",表示 10,11,12点都会触发。 ,
	 *            表示指定多个值，例如在周字段上设置 "MON,WED,FRI" 表示周一，周三和周五触发 /
	 *            用于递增触发。如在秒上面设置"5/15" 表示从5秒开始，每增15秒触发(5,20,35,50)。
	 *            在月字段上设置'1/3'所示每月1号开始，每隔三天触发一次。 L
	 *            表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]),
	 *            在周字段上表示星期六
	 *            ，相当于"7"或"SAT"。如果在"L"前加上数字，则表示该数据的最后一个。例如在周字段上设置"6L"这样的格式
	 *            ,则表示“本月最后一个星期五" W 表示离指定日期的最近那个工作日(周一至周五).
	 *            例如在日字段上设置"15W"，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发,
	 *            如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为
	 *            "1W",它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，"W"前只能设置具体的数字,
	 *            不允许区间"-"). #
	 *            序号(表示每月的第几个周几)，例如在周字段上设置"6#3"表示在每月的第三个周六.注意如果指定"#5"
	 *            ,正好第五周没有周六，则不会触发该配置(用在母亲节和父亲节再合适不过了) ；
	 * 
	 *            小提示： 'L'和 'W'可以一组合使用。如果在日字段上设置"LW",则表示在本月的最后一个工作日触发；
	 *            周字段的设置，若使用英文字母是不区分大小写的，即MON 与mon相同；
	 * 
	 *            常用示例:
	 * 
	 *            0 0 12 * * ? 每天12点触发 0 15 10 ? * * 每天10点15分触发 0 15 10 * * ?
	 *            每天10点15分触发 0 15 10 * * ? * 每天10点15分触发 0 15 10 * * ? 2005
	 *            2005年每天10点15分触发 0 * 14 * * ? 每天下午的 2点到2点59分每分触发 0 0/5 14 * * ?
	 *            每天下午的 2点到2点59分(整点开始，每隔5分触发) 0 0/5 14,18 * * ? 每天下午的
	 *            2点到2点59分、18点到18点59分(整点开始，每隔5分触发) 0 0-5 14 * * ? 每天下午的
	 *            2点到2点05分每分触发 0 10,44 14 ? 3 WED 3月分每周三下午的 2点10分和2点44分触发 0 15
	 *            10 ? * MON-FRI 从周一到周五每天上午的10点15分触发 0 15 10 15 * ?
	 *            每月15号上午10点15分触发 0 15 10 L * ? 每月最后一天的10点15分触发 0 15 10 ? * 6L
	 *            每月最后一周的星期五的10点15分触发 0 15 10 ? * 6L 2002-2005
	 *            从2002年到2005年每月最后一周的星期五的10点15分触发 0 15 10 ? * 6#3 每月的第三周的星期五开始触发
	 *            0 0 12 1/5 * ? 每月的第一个中午开始每隔5天触发一次 0 11 11 11 11 ? 每年的11月11号
	 *            11点11分触发(光棍节)
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void scheduleQuartzJob(String jobName, String cron, Class<? extends Job> jobClass, Map jobData) {
		try {
			JobDetail jobDetail = quartzScheduler.getJobDetail(jobName, Scheduler.DEFAULT_GROUP);
			if (jobDetail == null) {
				jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, jobClass);
				if (jobData != null)
					jobDetail.getJobDataMap().putAll(jobData);
				CronTrigger trigger = new CronTrigger(jobName + "Trigger", Scheduler.DEFAULT_GROUP);
				trigger.setCronExpression(new CronExpression(cron));
				quartzScheduler.scheduleJob(jobDetail, trigger);
				log.info("************* scheduleQuartzJob [" + jobName + "] with data " + jobData + " at " + cron);
			} else {
				if (jobData != null)
					jobDetail.getJobDataMap().putAll(jobData);
				CronTrigger trigger = (CronTrigger) quartzScheduler.getTrigger(jobName + "Trigger", Scheduler.DEFAULT_GROUP);
				if (trigger == null)
					trigger = new CronTrigger(jobName + "Trigger", Scheduler.DEFAULT_GROUP);
				trigger.setCronExpression(new CronExpression(cron));
				quartzScheduler.rescheduleJob(jobName, Scheduler.DEFAULT_GROUP, trigger);
				log.info("************* rescheduleQuartzJob [" + jobName + "] at " + cron);
			}
		} catch (Exception e) {
			throw new AppException("scheduleQuartzJob error:" + "jobName=" + jobName + " cron=" + cron + " jobClass="
					+ jobClass + " jobData=" + jobData, e);
		}
	}

	/**
	 * 在指定时间触发一次
	 * @param jobName 任务名
	 * @param fireTime 触发时间
	 * @param jobClass 任务类
	 * @param jobData 任务数据map
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void scheduleQuartzJobByTime(String jobName, Timestamp fireTime, Class<? extends Job> jobClass,
			Map jobData) {
		try {
			JobDetail jobDetail = quartzScheduler.getJobDetail(jobName, Scheduler.DEFAULT_GROUP);
			if (jobDetail == null) {
				jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, jobClass);
				if (jobData != null)
					jobDetail.getJobDataMap().putAll(jobData);
				quartzScheduler.scheduleJob(jobDetail, new SimpleTrigger(jobName + "Trigger", Scheduler.DEFAULT_GROUP,
						fireTime));
				log.info("************* scheduleQuartzJob [" + jobName + "] with data " + jobData + " at " + fireTime);
			} else {
				if (jobData != null)
					jobDetail.getJobDataMap().putAll(jobData);
				quartzScheduler.rescheduleJob(jobName, Scheduler.DEFAULT_GROUP, new SimpleTrigger(jobName + "Trigger",
						Scheduler.DEFAULT_GROUP, fireTime));
				log.info("************* rescheduleQuartzJob [" + jobName + "] at " + fireTime);
			}
		} catch (Exception e) {
			throw new AppException("scheduleQuartzJobByTime error:" + "jobName=" + jobName + " fireTime=" + fireTime
					+ " jobClass=" + jobClass + " jobData=" + jobData, e);
		}
	}
}
