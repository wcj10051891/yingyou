/*
SQLyog Ultimate v9.63 
MySQL - 5.5.43 : Database - rpg
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`rpg` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `rpg`;

/*Table structure for table `t_buff` */

DROP TABLE IF EXISTS `t_buff`;

CREATE TABLE `t_buff` (
  `id` int(11) NOT NULL COMMENT 'buff唯一id',
  `name` varchar(50) NOT NULL COMMENT 'buff名称',
  `desc` varchar(200) NOT NULL COMMENT 'buff描述',
  `param` varchar(500) DEFAULT NULL COMMENT '参数',
  `period` int(11) NOT NULL COMMENT '伤害间隔，毫秒',
  `count` int(11) NOT NULL COMMENT '伤害次数',
  `duration` int(11) NOT NULL COMMENT '持续时间，毫秒',
  `fixDamage` int(11) NOT NULL COMMENT '固定伤害',
  `hpMaxDamage` int(11) NOT NULL COMMENT '基于生命上限伤害百分比',
  `normalDamage` int(11) NOT NULL COMMENT '普通伤害百分比',
  `damageIncrease` int(11) DEFAULT NULL COMMENT '伤害增加百分比',
  `damageDecrease` int(11) DEFAULT NULL COMMENT '伤害减少百分比',
  `hitDamageIncrease` int(11) DEFAULT NULL COMMENT '受到伤害增加百分比',
  `hitDamageDecrease` int(11) DEFAULT NULL COMMENT '受到伤害减少百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='buff配置表';

/*Data for the table `t_buff` */

insert  into `t_buff`(`id`,`name`,`desc`,`param`,`period`,`count`,`duration`,`fixDamage`,`hpMaxDamage`,`normalDamage`,`damageIncrease`,`damageDecrease`,`hitDamageIncrease`,`hitDamageDecrease`) values (1,'buff1','这是buff1',NULL,0,0,300000,0,0,0,NULL,NULL,NULL,NULL);

/*Table structure for table `t_execution` */

DROP TABLE IF EXISTS `t_execution`;

CREATE TABLE `t_execution` (
  `key` varchar(50) NOT NULL COMMENT '简称',
  `clazz` varchar(200) NOT NULL COMMENT '类名',
  `method` varchar(50) NOT NULL COMMENT '方法名',
  `param` varchar(1000) NOT NULL COMMENT '默认参数',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='执行';

/*Data for the table `t_execution` */

insert  into `t_execution`(`key`,`clazz`,`method`,`param`) values ('lv','com.shadowgame.rpg.modules.exec.ExecService','checkLv','{\'lv\':10}');

/*Table structure for table `t_game_map` */

DROP TABLE IF EXISTS `t_game_map`;

CREATE TABLE `t_game_map` (
  `id` int(11) NOT NULL COMMENT '地图id',
  `name` varchar(50) NOT NULL COMMENT '地图名称',
  `width` int(11) NOT NULL COMMENT '宽',
  `height` int(11) NOT NULL COMMENT '高',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地图';

/*Data for the table `t_game_map` */

insert  into `t_game_map`(`id`,`name`,`width`,`height`) values (1,'新手村',1000,1000);

/*Table structure for table `t_item` */

DROP TABLE IF EXISTS `t_item`;

CREATE TABLE `t_item` (
  `id` int(11) NOT NULL COMMENT '道具id',
  `name` varchar(50) NOT NULL COMMENT '道具名称',
  `desc` varchar(200) NOT NULL COMMENT '道具描述',
  `playerLv` int(11) NOT NULL COMMENT '需求玩家等级',
  `parentType` int(11) NOT NULL COMMENT '父分类',
  `itemType` int(11) NOT NULL COMMENT '道具分类',
  `bindType` int(11) NOT NULL COMMENT '绑定类型（0不绑定1获得绑定2使用绑定）',
  `quality` int(11) NOT NULL COMMENT '品质',
  `maxStack` int(11) NOT NULL COMMENT '最大堆叠数',
  `function` varchar(100) NOT NULL COMMENT '道具功能列表',
  `extAttribute` text COMMENT '扩展属性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='道具';

/*Data for the table `t_item` */

/*Table structure for table `t_mission` */

DROP TABLE IF EXISTS `t_mission`;

CREATE TABLE `t_mission` (
  `id` int(11) NOT NULL COMMENT '任务id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `acceptCond1` text COMMENT '接受条件1',
  `acceptCond2` text COMMENT '接受条件2',
  `acceptCond3` text COMMENT '接受条件3',
  `acceptCond4` text COMMENT '接受条件4',
  `acceptCond5` text COMMENT '接受条件5',
  `goal1` text COMMENT '任务目标1',
  `goal2` text COMMENT '任务目标2',
  `goal3` text COMMENT '任务目标3',
  `goal4` text COMMENT '任务目标4',
  `goal5` text COMMENT '任务目标5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务配置';

/*Data for the table `t_mission` */

insert  into `t_mission`(`id`,`name`,`acceptCond1`,`acceptCond2`,`acceptCond3`,`acceptCond4`,`acceptCond5`,`goal1`,`goal2`,`goal3`,`goal4`,`goal5`) values (1,'杀怪',NULL,NULL,NULL,NULL,NULL,'{\'key\':\'killMonster\',\'param\':{\'monsterId\':1,\'count\':1}}',NULL,NULL,NULL,NULL);

/*Table structure for table `t_mission_goal` */

DROP TABLE IF EXISTS `t_mission_goal`;

CREATE TABLE `t_mission_goal` (
  `key` varchar(50) NOT NULL COMMENT '简称',
  `clazz` varchar(200) NOT NULL COMMENT '实现类',
  `param` varchar(1000) NOT NULL COMMENT '默认参数',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务目标';

/*Data for the table `t_mission_goal` */

insert  into `t_mission_goal`(`key`,`clazz`,`param`) values ('killMonster','com.shadowgame.rpg.modules.mission.goals.KillMonsterMissionGoal','{\'id\':1,\'count\':1}');

/*Table structure for table `t_monster` */

DROP TABLE IF EXISTS `t_monster`;

CREATE TABLE `t_monster` (
  `id` int(11) NOT NULL COMMENT '怪物id',
  `name` varchar(50) NOT NULL COMMENT '怪物名称',
  `boss` bit(1) NOT NULL COMMENT '是否是boss',
  `mapId` int(11) NOT NULL COMMENT '所属地图id',
  `x` int(11) NOT NULL COMMENT '坐标x',
  `y` int(11) NOT NULL COMMENT '坐标y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='怪物表';

/*Data for the table `t_monster` */

insert  into `t_monster`(`id`,`name`,`boss`,`mapId`,`x`,`y`) values (1,'怪物1','',1,0,0);

/*Table structure for table `t_player` */

DROP TABLE IF EXISTS `t_player`;

CREATE TABLE `t_player` (
  `id` bigint(11) NOT NULL COMMENT '玩家id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `vocation` tinyint(4) NOT NULL DEFAULT '1' COMMENT '职业，1战士2法师3弓箭手',
  `lv` int(11) NOT NULL COMMENT '等级',
  `exp` int(11) NOT NULL COMMENT '当前经验',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `loginTime` timestamp NULL DEFAULT NULL COMMENT '上次登录时间',
  `logoutTime` timestamp NULL DEFAULT NULL COMMENT '上次登出时间',
  `daily` text COMMENT '每日属性',
  `extAttribute` text COMMENT '扩展属性',
  `lastMapId` int(11) DEFAULT NULL COMMENT '离线所在地图配置id',
  `lastInstanceId` int(11) DEFAULT NULL COMMENT '离线所在地图副本id',
  `lastMapX` int(11) DEFAULT NULL COMMENT '离线所在副本坐标x',
  `lastMapY` int(11) DEFAULT NULL COMMENT '离线所在副本坐标y',
  `skill` text COMMENT '玩家技能',
  `buff` text COMMENT '玩家buff',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家';

/*Data for the table `t_player` */

insert  into `t_player`(`id`,`username`,`nickname`,`vocation`,`lv`,`exp`,`createTime`,`loginTime`,`logoutTime`,`daily`,`extAttribute`,`lastMapId`,`lastInstanceId`,`lastMapX`,`lastMapY`,`skill`,`buff`) values (4672337295849525248,'u1','u1',1,1,0,'2015-06-13 03:16:07',NULL,NULL,'{\"date\":\"20150626\"}','{}',NULL,NULL,NULL,NULL,NULL,NULL),(4676265064748060672,'123','玩家1',2,1,0,'2015-06-27 01:50:25',NULL,NULL,'{\"date\":\"20150702\"}','{}',1,1435825476,0,0,'[{\"id\":1,\"lv\":1}]','[]'),(4676266992980103168,'123','玩家2',2,1,0,'2015-06-27 02:00:27',NULL,NULL,'{}','{}',NULL,NULL,NULL,NULL,NULL,NULL),(4676267820664328192,'123','玩家3',3,1,0,'2015-06-27 02:06:29',NULL,NULL,'{}','{}',NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_player_item` */

DROP TABLE IF EXISTS `t_player_item`;

CREATE TABLE `t_player_item` (
  `id` bigint(20) NOT NULL COMMENT '玩家道具id',
  `itemId` int(11) NOT NULL COMMENT '系统道具id',
  `playerId` bigint(11) NOT NULL COMMENT '玩家id',
  `binding` bit(1) NOT NULL COMMENT '是否绑定',
  `num` int(11) NOT NULL COMMENT '堆叠数',
  `strengthenLv` int(11) NOT NULL COMMENT '强化等级',
  `hole` text COMMENT '宝石镶嵌孔',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家道具';

/*Data for the table `t_player_item` */

/*Table structure for table `t_player_knapsack` */

DROP TABLE IF EXISTS `t_player_knapsack`;

CREATE TABLE `t_player_knapsack` (
  `id` bigint(20) NOT NULL COMMENT '玩家id',
  `items` text COMMENT '道具列表',
  `capacity` int(11) NOT NULL COMMENT '当前容量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家背包';

/*Data for the table `t_player_knapsack` */

insert  into `t_player_knapsack`(`id`,`items`,`capacity`) values (4672337295849525248,'[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]',100),(4676265064748060672,'[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]',100);

/*Table structure for table `t_player_mission` */

DROP TABLE IF EXISTS `t_player_mission`;

CREATE TABLE `t_player_mission` (
  `id` bigint(20) NOT NULL COMMENT '玩家id',
  `acceptMission` text COMMENT '已接任务',
  `finishMission` text COMMENT '已完成任务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家任务';

/*Data for the table `t_player_mission` */

insert  into `t_player_mission`(`id`,`acceptMission`,`finishMission`) values (4672337295849525248,'[]','[]'),(4676265064748060672,'[]','[]');

/*Table structure for table `t_skill` */

DROP TABLE IF EXISTS `t_skill`;

CREATE TABLE `t_skill` (
  `id` int(11) NOT NULL COMMENT '技能id',
  `name` varchar(50) NOT NULL COMMENT '技能名称',
  `desc` varchar(200) NOT NULL COMMENT '描述',
  `vocation` int(11) NOT NULL COMMENT '职业id',
  `rangeType` tinyint(11) NOT NULL COMMENT '作用范围类型，1矩形2圆形3扇形',
  `rangePosition` tinyint(11) NOT NULL COMMENT '作用范围位置，1施法者2目标',
  `rangeDistance` int(11) NOT NULL COMMENT '作用范围距离',
  `rangeAngle` int(11) NOT NULL COMMENT '角度',
  `rangeWidth` int(11) NOT NULL COMMENT '宽度',
  `delay` int(11) NOT NULL COMMENT '延迟时间，毫秒',
  `period` int(11) NOT NULL COMMENT '作用间隔，毫秒',
  `count` int(11) NOT NULL COMMENT '作用次数',
  `param` varchar(500) DEFAULT NULL COMMENT '技能参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='技能';

/*Data for the table `t_skill` */

insert  into `t_skill`(`id`,`name`,`desc`,`vocation`,`rangeType`,`rangePosition`,`rangeDistance`,`rangeAngle`,`rangeWidth`,`delay`,`period`,`count`,`param`) values (1,'技能1','这是技能1',1,1,1,10,0,0,0,0,0,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
