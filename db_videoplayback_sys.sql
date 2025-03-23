/*
SQLyog Community v13.2.0 (64 bit)
MySQL - 8.0.12 : Database - db_videoplayback_sys
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_videoplayback_sys` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_videoplayback_sys`;

/*Table structure for table `banner` */

DROP TABLE IF EXISTS `banner`;

CREATE TABLE `banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '轮播图编号',
  `img` varchar(200) DEFAULT NULL COMMENT '图片',
  `url` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `index_radio` varchar(20) DEFAULT NULL COMMENT '是否首页',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='轮播图';

/*Data for the table `banner` */

insert  into `banner`(`id`,`img`,`url`,`index_radio`) values 
(5,'http://localhost:9090/file/download/f0e0f31040d34deb81a20ed998108aa8.png',NULL,'是'),
(6,'http://localhost:9090/file/download/500136d330274a22b865a13be387904b.png',NULL,'是'),
(7,'http://localhost:9090/file/download/bfe369e608344197b54b67d1a651f768.png',NULL,'否');

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='视频分类';

/*Data for the table `category` */

insert  into `category`(`id`,`name`) values 
(1,'娱乐'),
(2,'生活'),
(3,'游戏'),
(4,'知识'),
(5,'影视'),
(6,'运动'),
(7,'美食');

/*Table structure for table `collect` */

DROP TABLE IF EXISTS `collect`;

CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户',
  `vedio_id` int(11) DEFAULT NULL COMMENT '视频',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`user_id`,`vedio_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='视频收藏';

/*Data for the table `collect` */

insert  into `collect`(`id`,`user_id`,`vedio_id`,`create_time`,`update_time`) values 
(2,22,1,'2024-04-01 08:03:56','2024-04-01 08:03:56'),
(4,22,4,'2024-04-01 09:50:54','2024-04-01 09:50:54');

/*Table structure for table `comments` */

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论编号',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '评论内容',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `user_id` int(11) DEFAULT NULL COMMENT '用户',
  `vedio_id` int(11) DEFAULT NULL COMMENT '视频编号',
  `pid` int(11) DEFAULT NULL COMMENT '父评论ID',
  `puser_id` int(11) DEFAULT NULL COMMENT '父级用户ID',
  `score` int(11) DEFAULT NULL COMMENT '评论星级',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='视频评论';

/*Data for the table `comments` */

insert  into `comments`(`id`,`content`,`create_time`,`update_time`,`user_id`,`vedio_id`,`pid`,`puser_id`,`score`) values 
(1,'不错哦','2024-04-01 08:05:13','2024-04-01 08:05:13',22,1,NULL,NULL,5);

/*Table structure for table `dict` */

DROP TABLE IF EXISTS `dict`;

CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '编码',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `deleted` int(11) DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `c_d` (`code`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='数据字典';

/*Data for the table `dict` */

insert  into `dict`(`id`,`code`,`value`,`type`,`deleted`) values 
(2,'message','message','icon',0),
(3,'menu','menu','icon',0),
(4,'grid','grid','icon',0),
(5,'house','house','icon',0),
(6,'user','user','icon',0),
(7,'file','files','icon',0),
(8,'money','money','icon',0),
(9,'school','school','icon',0),
(10,'notebook','notebook','icon',0),
(11,'coin','coin','icon',0),
(12,'set-up','set-up','icon',0),
(13,'postcard','postcard','icon',0),
(14,'food','food','icon',0),
(15,'position','position','icon',0),
(16,'chat-line-round','chat-line-round','icon',0),
(17,'chat-dot-round','chat-dot-round','icon',0),
(18,'setting','setting','icon',0),
(19,'comment','comment','icon',0);

/*Table structure for table `feedback` */

DROP TABLE IF EXISTS `feedback`;

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) DEFAULT NULL COMMENT '反馈标题',
  `content` text COMMENT '反馈内容',
  `user_id` int(11) DEFAULT NULL COMMENT '反馈用户',
  `create_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `reply` varchar(200) DEFAULT NULL COMMENT '平台回复',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户反馈';

/*Data for the table `feedback` */

insert  into `feedback`(`id`,`name`,`content`,`user_id`,`create_time`,`reply`) values 
(1,'测试反馈标题','<p>测试反馈内容</p>',22,'2024-04-01 10:45:01','我们会继续改进的！');

/*Table structure for table `follow` */

DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(11) DEFAULT NULL COMMENT '关注用户',
  `follow_id` int(11) DEFAULT NULL COMMENT '被关注用户',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户关注';

/*Data for the table `follow` */

insert  into `follow`(`id`,`user_id`,`follow_id`) values 
(12,22,23);

/*Table structure for table `member` */

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `name` varchar(200) DEFAULT NULL COMMENT '姓名',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户';

/*Data for the table `member` */

insert  into `member`(`id`,`username`,`name`,`user_id`) values 
(1,'zhangsan','张三',22),
(2,'lisi','李四',23);

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统公告';

/*Data for the table `notice` */

insert  into `notice`(`id`,`name`,`content`,`create_time`,`user_id`) values 
(8,'项目成立','<p>项目成立</p>','2023-07-27 09:06:21',1);

/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径',
  `orders` int(11) DEFAULT '1' COMMENT '顺序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'grid' COMMENT '图标',
  `page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '页面路径',
  `auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限',
  `pid` int(11) DEFAULT NULL COMMENT '父级编号',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `hide` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `a_d_index` (`auth`,`deleted`) USING BTREE,
  UNIQUE KEY `p_p_d_index` (`path`,`page`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=602 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='权限菜单';

/*Data for the table `sys_permission` */

insert  into `sys_permission`(`id`,`name`,`path`,`orders`,`icon`,`page`,`auth`,`pid`,`deleted`,`create_time`,`update_time`,`type`,`hide`) values 
(1,'系统管理','',2,'menu',NULL,NULL,NULL,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',1,0),
(3,'用户管理','user',1,'user','User','user.list',1,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',2,0),
(4,'用户新增','',1,NULL,'','user.add',3,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',3,0),
(8,'用户编辑','',1,NULL,NULL,'user.edit',3,0,NULL,'2023-01-28 11:45:21',3,0),
(9,'用户删除',NULL,1,NULL,NULL,'user.delete',3,0,'2023-01-29 11:04:15','2023-01-29 11:04:15',3,0),
(10,'角色管理','role',1,'grid','Role','role.list',1,0,'2023-01-31 20:32:59','2023-01-31 20:32:59',2,0),
(11,'权限管理','permission',1,'position','Permission','permission.list',1,0,'2023-01-31 20:33:25','2023-01-31 20:33:25',2,0),
(12,'首页','home',1,'house','Home',NULL,NULL,0,'2023-01-31 21:03:00','2023-01-31 21:03:00',2,0),
(13,'数据字典管理','dict',1,'grid','Dict','dict.list',1,0,'2023-02-02 20:41:32','2023-02-02 20:41:32',2,0),
(14,'批量删除',NULL,1,'',NULL,'user.deleteBatch',3,0,'2023-02-02 22:32:22','2023-02-02 22:32:22',3,0),
(15,'用户导入',NULL,1,NULL,NULL,'user.import',3,0,'2023-02-02 22:32:53','2023-02-02 22:32:53',3,0),
(16,'用户导出',NULL,1,NULL,NULL,'user.export',3,0,'2023-02-02 22:33:08','2023-02-02 22:33:08',3,0),
(21,'角色新增',NULL,1,NULL,'','role.add',10,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',3,0),
(22,'角色编辑',NULL,1,NULL,NULL,'role.edit',10,0,NULL,'2023-01-28 11:45:21',3,0),
(23,'角色删除',NULL,1,NULL,NULL,'role.delete',10,0,'2023-01-29 11:04:15','2023-01-29 11:04:15',3,0),
(25,'批量删除',NULL,1,NULL,NULL,'role.deleteBatch',10,0,'2023-02-02 22:32:22','2023-02-02 22:32:22',3,0),
(26,'角色导入',NULL,1,NULL,NULL,'role.import',10,0,'2023-02-02 22:32:53','2023-02-02 22:32:53',3,0),
(27,'角色导出',NULL,1,NULL,NULL,'role.export',10,0,'2023-02-02 22:33:08','2023-02-02 22:33:08',3,0),
(30,'权限新增',NULL,1,NULL,'','permission.add',11,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',3,0),
(31,'权限编辑',NULL,1,NULL,NULL,'permission.edit',11,0,NULL,'2023-01-28 11:45:21',3,0),
(32,'权限删除',NULL,1,NULL,NULL,'permission.delete',11,0,'2023-01-29 11:04:15','2023-01-29 11:04:15',3,0),
(34,'权限导入',NULL,1,NULL,NULL,'permission.import',11,0,'2023-02-02 22:32:53','2023-02-02 22:32:53',3,0),
(35,'权限导出',NULL,1,NULL,NULL,'permission.export',11,0,'2023-02-02 22:33:08','2023-02-02 22:33:08',3,0),
(37,'数据字典新增',NULL,1,NULL,'','dict.add',13,0,'2023-01-16 20:45:51','2023-01-16 20:45:51',3,0),
(38,'数据字典编辑',NULL,1,NULL,NULL,'dict.edit',13,0,NULL,'2023-01-28 11:45:21',3,0),
(39,'数据字典删除',NULL,1,NULL,NULL,'dict.delete',13,0,'2023-01-29 11:04:15','2023-01-29 11:04:15',3,0),
(40,'批量删除',NULL,1,NULL,NULL,'dict.deleteBatch',13,0,'2023-02-02 22:32:22','2023-02-02 22:32:22',3,0),
(41,'数据字典导入',NULL,1,NULL,NULL,'dict.import',13,0,'2023-02-02 22:32:53','2023-02-02 22:32:53',3,0),
(42,'数据字典导出',NULL,1,NULL,NULL,'dict.export',13,0,'2023-02-02 22:33:08','2023-02-02 22:33:08',3,0),
(186,'数据图表','dashboard',1,'grid','Dashboard',NULL,NULL,186,NULL,'2023-04-18 10:56:38',2,0),
(505,'公告管理','notice',1,'comment','Notice',NULL,NULL,0,NULL,NULL,2,0),
(506,'公告查询',NULL,1,'grid',NULL,'notice.list',505,0,NULL,NULL,3,0),
(507,'公告新增',NULL,1,'grid',NULL,'notice.add',505,0,NULL,NULL,3,0),
(508,'公告导入',NULL,1,'grid',NULL,'notice.import',505,0,NULL,NULL,3,0),
(509,'公告导出',NULL,1,'grid',NULL,'notice.export',505,0,NULL,NULL,3,0),
(510,'批量删除',NULL,1,'grid',NULL,'notice.deleteBatch',505,0,NULL,NULL,3,0),
(511,'公告编辑',NULL,1,'grid',NULL,'notice.edit',505,0,NULL,NULL,3,0),
(512,'公告删除',NULL,1,'grid',NULL,'notice.delete',505,0,NULL,NULL,3,0),
(521,'用户管理','member',1,'grid','Member',NULL,NULL,0,NULL,NULL,2,0),
(522,'用户查询',NULL,1,'grid',NULL,'member.list',521,0,NULL,NULL,3,0),
(523,'用户新增',NULL,1,'grid',NULL,'member.add',521,0,NULL,NULL,3,0),
(524,'用户导入',NULL,1,'grid',NULL,'member.import',521,0,NULL,NULL,3,0),
(525,'用户导出',NULL,1,'grid',NULL,'member.export',521,0,NULL,NULL,3,0),
(526,'批量删除',NULL,1,'grid',NULL,'member.deleteBatch',521,0,NULL,NULL,3,0),
(527,'用户编辑',NULL,1,'grid',NULL,'member.edit',521,0,NULL,NULL,3,0),
(528,'用户删除',NULL,1,'grid',NULL,'member.delete',521,0,NULL,NULL,3,0),
(529,'视频分类管理','category',1,'grid','Category',NULL,NULL,0,NULL,NULL,2,0),
(530,'视频分类查询',NULL,1,'grid',NULL,'category.list',529,0,NULL,NULL,3,0),
(531,'视频分类新增',NULL,1,'grid',NULL,'category.add',529,0,NULL,NULL,3,0),
(532,'视频分类导入',NULL,1,'grid',NULL,'category.import',529,0,NULL,NULL,3,0),
(533,'视频分类导出',NULL,1,'grid',NULL,'category.export',529,0,NULL,NULL,3,0),
(534,'批量删除',NULL,1,'grid',NULL,'category.deleteBatch',529,0,NULL,NULL,3,0),
(535,'视频分类编辑',NULL,1,'grid',NULL,'category.edit',529,0,NULL,NULL,3,0),
(536,'视频分类删除',NULL,1,'grid',NULL,'category.delete',529,0,NULL,NULL,3,0),
(537,'视频管理','vedio',1,'grid','Vedio',NULL,NULL,0,NULL,NULL,2,0),
(538,'视频查询',NULL,1,'grid',NULL,'vedio.list',537,0,NULL,NULL,3,0),
(539,'视频新增',NULL,1,'grid',NULL,'vedio.add',537,0,NULL,NULL,3,0),
(540,'视频导入',NULL,1,'grid',NULL,'vedio.import',537,0,NULL,NULL,3,0),
(541,'视频导出',NULL,1,'grid',NULL,'vedio.export',537,0,NULL,NULL,3,0),
(542,'批量删除',NULL,1,'grid',NULL,'vedio.deleteBatch',537,0,NULL,NULL,3,0),
(543,'视频编辑',NULL,1,'grid',NULL,'vedio.edit',537,0,NULL,NULL,3,0),
(544,'视频删除',NULL,1,'grid',NULL,'vedio.delete',537,0,NULL,NULL,3,0),
(545,'用户反馈管理','feedback',1,'grid','Feedback',NULL,NULL,0,NULL,NULL,2,0),
(546,'用户反馈查询',NULL,1,'grid',NULL,'feedback.list',545,0,NULL,NULL,3,0),
(547,'用户反馈新增',NULL,1,'grid',NULL,'feedback.add',545,0,NULL,NULL,3,0),
(548,'用户反馈导入',NULL,1,'grid',NULL,'feedback.import',545,0,NULL,NULL,3,0),
(549,'用户反馈导出',NULL,1,'grid',NULL,'feedback.export',545,0,NULL,NULL,3,0),
(550,'批量删除',NULL,1,'grid',NULL,'feedback.deleteBatch',545,0,NULL,NULL,3,0),
(551,'用户反馈编辑',NULL,1,'grid',NULL,'feedback.edit',545,0,NULL,NULL,3,0),
(552,'用户反馈删除',NULL,1,'grid',NULL,'feedback.delete',545,0,NULL,NULL,3,0),
(553,'视频评论管理','comments',1,'grid','Comments',NULL,NULL,0,NULL,NULL,2,0),
(554,'视频评论查询',NULL,1,'grid',NULL,'comments.list',553,0,NULL,NULL,3,0),
(555,'视频评论新增',NULL,1,'grid',NULL,'comments.add',553,0,NULL,NULL,3,0),
(556,'视频评论导入',NULL,1,'grid',NULL,'comments.import',553,0,NULL,NULL,3,0),
(557,'视频评论导出',NULL,1,'grid',NULL,'comments.export',553,0,NULL,NULL,3,0),
(558,'批量删除',NULL,1,'grid',NULL,'comments.deleteBatch',553,0,NULL,NULL,3,0),
(559,'视频评论编辑',NULL,1,'grid',NULL,'comments.edit',553,0,NULL,NULL,3,0),
(560,'视频评论删除',NULL,1,'grid',NULL,'comments.delete',553,0,NULL,NULL,3,0),
(561,'视频收藏管理','collect',1,'grid','Collect',NULL,NULL,0,NULL,NULL,2,0),
(562,'视频收藏查询',NULL,1,'grid',NULL,'collect.list',561,0,NULL,NULL,3,0),
(563,'视频收藏新增',NULL,1,'grid',NULL,'collect.add',561,0,NULL,NULL,3,0),
(564,'视频收藏导入',NULL,1,'grid',NULL,'collect.import',561,0,NULL,NULL,3,0),
(565,'视频收藏导出',NULL,1,'grid',NULL,'collect.export',561,0,NULL,NULL,3,0),
(566,'批量删除',NULL,1,'grid',NULL,'collect.deleteBatch',561,0,NULL,NULL,3,0),
(567,'视频收藏编辑',NULL,1,'grid',NULL,'collect.edit',561,0,NULL,NULL,3,0),
(568,'视频收藏删除',NULL,1,'grid',NULL,'collect.delete',561,0,NULL,NULL,3,0),
(569,'标签管理','tags',1,'grid','Tags',NULL,NULL,0,NULL,NULL,2,0),
(570,'标签查询',NULL,1,'grid',NULL,'tags.list',569,0,NULL,NULL,3,0),
(571,'标签新增',NULL,1,'grid',NULL,'tags.add',569,0,NULL,NULL,3,0),
(572,'标签导入',NULL,1,'grid',NULL,'tags.import',569,0,NULL,NULL,3,0),
(573,'标签导出',NULL,1,'grid',NULL,'tags.export',569,0,NULL,NULL,3,0),
(574,'批量删除',NULL,1,'grid',NULL,'tags.deleteBatch',569,0,NULL,NULL,3,0),
(575,'标签编辑',NULL,1,'grid',NULL,'tags.edit',569,0,NULL,NULL,3,0),
(576,'标签删除',NULL,1,'grid',NULL,'tags.delete',569,0,NULL,NULL,3,0),
(577,'视频标签关联管理','vediotags',1,'grid','Vediotags',NULL,NULL,0,NULL,NULL,2,0),
(578,'视频标签关联查询',NULL,1,'grid',NULL,'vediotags.list',577,0,NULL,NULL,3,0),
(579,'视频标签关联新增',NULL,1,'grid',NULL,'vediotags.add',577,0,NULL,NULL,3,0),
(580,'视频标签关联导入',NULL,1,'grid',NULL,'vediotags.import',577,0,NULL,NULL,3,0),
(581,'视频标签关联导出',NULL,1,'grid',NULL,'vediotags.export',577,0,NULL,NULL,3,0),
(582,'批量删除',NULL,1,'grid',NULL,'vediotags.deleteBatch',577,0,NULL,NULL,3,0),
(583,'视频标签关联编辑',NULL,1,'grid',NULL,'vediotags.edit',577,0,NULL,NULL,3,0),
(584,'视频标签关联删除',NULL,1,'grid',NULL,'vediotags.delete',577,0,NULL,NULL,3,0),
(585,'用户关注管理','follow',1,'grid','Follow',NULL,NULL,0,NULL,NULL,2,0),
(586,'用户关注查询',NULL,1,'grid',NULL,'follow.list',585,0,NULL,NULL,3,0),
(587,'用户关注新增',NULL,1,'grid',NULL,'follow.add',585,0,NULL,NULL,3,0),
(588,'用户关注导入',NULL,1,'grid',NULL,'follow.import',585,0,NULL,NULL,3,0),
(589,'用户关注导出',NULL,1,'grid',NULL,'follow.export',585,0,NULL,NULL,3,0),
(590,'批量删除',NULL,1,'grid',NULL,'follow.deleteBatch',585,0,NULL,NULL,3,0),
(591,'用户关注编辑',NULL,1,'grid',NULL,'follow.edit',585,0,NULL,NULL,3,0),
(592,'用户关注删除',NULL,1,'grid',NULL,'follow.delete',585,0,NULL,NULL,3,0),
(593,'轮播图管理','banner',1,'grid','Banner',NULL,NULL,0,NULL,NULL,2,0),
(594,'轮播图查询',NULL,1,'grid',NULL,'banner.list',593,0,NULL,NULL,3,0),
(595,'轮播图新增',NULL,1,'grid',NULL,'banner.add',593,0,NULL,NULL,3,0),
(596,'轮播图导入',NULL,1,'grid',NULL,'banner.import',593,0,NULL,NULL,3,0),
(597,'轮播图导出',NULL,1,'grid',NULL,'banner.export',593,0,NULL,NULL,3,0),
(598,'批量删除',NULL,1,'grid',NULL,'banner.deleteBatch',593,0,NULL,NULL,3,0),
(599,'轮播图编辑',NULL,1,'grid',NULL,'banner.edit',593,0,NULL,NULL,3,0),
(600,'轮播图删除',NULL,1,'grid',NULL,'banner.delete',593,0,NULL,NULL,3,0),
(601,'数据统计','statistics',1,'grid','Statistics',NULL,NULL,0,NULL,NULL,2,0);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯一标识',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `flag_deleted_idnex` (`flag`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`name`,`flag`,`deleted`,`create_time`,`update_time`) values 
(1,'管理员','ADMIN',0,'2023-01-16 19:49:44','2023-01-16 19:49:55'),
(4,'用户','member',0,'2024-04-01 00:28:12','2024-04-01 00:28:12');

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` int(11) NOT NULL COMMENT '角色',
  `permission_id` int(11) NOT NULL COMMENT '权限',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role_id` (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5884 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限';

/*Data for the table `sys_role_permission` */

insert  into `sys_role_permission`(`id`,`role_id`,`permission_id`) values 
(5856,1,1),
(5857,1,3),
(5858,1,4),
(5859,1,8),
(5860,1,9),
(5864,1,10),
(5871,1,11),
(5782,1,12),
(5877,1,13),
(5861,1,14),
(5862,1,15),
(5863,1,16),
(5865,1,21),
(5866,1,22),
(5867,1,23),
(5868,1,25),
(5869,1,26),
(5870,1,27),
(5872,1,30),
(5873,1,31),
(5874,1,32),
(5875,1,34),
(5876,1,35),
(5878,1,37),
(5879,1,38),
(5880,1,39),
(5881,1,40),
(5882,1,41),
(5883,1,42),
(5783,1,505),
(5784,1,506),
(5785,1,507),
(5786,1,508),
(5787,1,509),
(5788,1,510),
(5789,1,511),
(5790,1,512),
(5791,1,521),
(5792,1,522),
(5793,1,523),
(5794,1,524),
(5795,1,525),
(5796,1,526),
(5797,1,527),
(5798,1,528),
(5799,1,529),
(5800,1,530),
(5801,1,531),
(5802,1,532),
(5803,1,533),
(5804,1,534),
(5805,1,535),
(5806,1,536),
(5807,1,537),
(5808,1,538),
(5809,1,539),
(5810,1,540),
(5811,1,541),
(5812,1,542),
(5813,1,543),
(5814,1,544),
(5815,1,545),
(5816,1,546),
(5817,1,547),
(5818,1,548),
(5819,1,549),
(5820,1,550),
(5821,1,551),
(5822,1,552),
(5823,1,553),
(5824,1,554),
(5825,1,555),
(5826,1,556),
(5827,1,557),
(5828,1,558),
(5829,1,559),
(5830,1,560),
(5831,1,561),
(5832,1,562),
(5833,1,563),
(5834,1,564),
(5835,1,565),
(5836,1,566),
(5837,1,567),
(5838,1,568),
(5839,1,569),
(5840,1,570),
(5841,1,571),
(5842,1,572),
(5843,1,573),
(5844,1,574),
(5845,1,575),
(5846,1,576),
(5847,1,593),
(5848,1,594),
(5849,1,595),
(5850,1,596),
(5851,1,597),
(5852,1,598),
(5853,1,599),
(5854,1,600),
(5855,1,601),
(4644,3,12),
(4645,3,490),
(4646,3,491),
(4647,3,494),
(4648,3,495),
(4649,3,496),
(5663,4,12),
(5661,4,537),
(5664,4,538),
(5665,4,539),
(5666,4,543),
(5667,4,544),
(5662,4,545),
(5668,4,546),
(5669,4,547),
(5670,4,551),
(5671,4,552);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户唯一id',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `score` int(11) DEFAULT '0' COMMENT '积分',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uid_index` (`uid`) USING BTREE,
  UNIQUE KEY `username_index` (`username`,`deleted`) USING BTREE,
  UNIQUE KEY `email_index` (`email`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`username`,`password`,`name`,`email`,`address`,`uid`,`deleted`,`create_time`,`update_time`,`avatar`,`role`,`score`) values 
(1,'admin','$2a$10$sz5GIsQI162NS90.iuF6KuqUq6VeUb4uXztDDMDlry44Hazid/lhm','管理员','admin@qq.com','广州南沙区','4918ea50c06a458f94878abe741b4f51',0,'2022-12-09 20:08:17','2022-12-09 20:08:17','http://localhost:9090/file/download/cce151d3972b4780acf5a115f6a55f48.png','ADMIN',0),
(22,'zhangsan','$2a$10$1d4bkNaN59rm9XNUWtoovO2emcfGurjyXGW7l9zL5azmCshhDbAde','张三','zhangsan@qq.com',NULL,'940d5c35a82f449bb928edbf18863b0c',0,'2024-04-01 07:44:18','2024-04-01 07:44:18','http://localhost:9090/file/download/42e3bff8f2244789885dcdc323ec219f.jpg','member',0),
(23,'lisi','$2a$10$2ssHhpaPoSfc7TUzj6Ckl.DIiuJAY2HKm0kNHIhg9vl/VrrwK1.9C','李四','lisi@qq.com',NULL,'a401231ecb144c9786ceed46a42dad68',0,'2024-04-01 08:10:15','2024-04-01 08:10:15','http://localhost:9090/file/download/c987f4f5bed148b7bac5461b496c029e.jpg','member',0);

/*Table structure for table `tags` */

DROP TABLE IF EXISTS `tags`;

CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) DEFAULT NULL COMMENT '标签名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='标签';

/*Data for the table `tags` */

insert  into `tags`(`id`,`name`) values 
(1,'沈腾'),
(2,'开心麻花'),
(3,'恶搞'),
(4,'素人'),
(5,'预告片'),
(6,'电影剪辑'),
(7,'生活小技巧');

/*Table structure for table `vedio` */

DROP TABLE IF EXISTS `vedio`;

CREATE TABLE `vedio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `category_id` int(11) DEFAULT NULL COMMENT '视频分类',
  `name` varchar(200) DEFAULT NULL COMMENT '视频名称',
  `img` varchar(200) DEFAULT NULL COMMENT '视频封面',
  `content` text COMMENT '视频简介',
  `vedio` varchar(200) DEFAULT NULL COMMENT '视频文件',
  `user_id` int(11) DEFAULT NULL COMMENT '上传用户',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `state_radio` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '审核中' COMMENT '状态,审核中|审核通过|审核失败',
  `reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `views` int(11) DEFAULT NULL COMMENT '访问量',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='视频';

/*Data for the table `vedio` */

insert  into `vedio`(`id`,`category_id`,`name`,`img`,`content`,`vedio`,`user_id`,`create_time`,`state_radio`,`reason`,`views`) values 
(1,5,'地狱男爵5最新预告片','http://localhost:9090/file/download/a62fc34a786748ffbbbb6f47538526cb.png','<p><span style=\"font-size: 14px;\">《地狱男爵》（Hellboy）由九零年代畅销漫画改编。影片由吉尔莫·德尔·托罗执导，朗·普尔曼、塞尔玛·布莱尔、鲁克·高斯主演。影片讲述了二战时期，撒旦之子地狱男爵（朗·普尔曼）被1944年纳粹党羽从事秘密实验召唤出，此后他被美国教授布鲁姆（约翰·赫特）收养避免危害人间，当他逐渐成熟显露出狰狞的外表后，被派遣成为特殊调查员，任务期间他与纳粹邪恶巫师重新联络上，他必须在正义与邪恶间作一选择</span></p>','http://localhost:9090/file/download/ada99eac223b4a39bf205bbdfa93c44a.mp4',22,'2024-04-01 07:55:54','审核通过',NULL,20),
(2,2,'发一段小视频测试一下','http://localhost:9090/file/download/88aef30d201b4182871d922858366e92.png','<p>发一段小视频测试一下</p>','http://localhost:9090/file/download/53fee0f4196d4e29af3aa721f7482133.mp4',22,'2024-04-01 08:07:24','审核通过',NULL,8),
(3,5,'飞驰人生2最强预告片','http://localhost:9090/file/download/eafcbef3681f4696b5a05ede66834689.png','<p>飞驰人生2最强预告片</p>','http://localhost:9090/file/download/96969e25de7544efa272bdc458cd737b.mp4',23,'2024-04-01 08:11:51','审核通过',NULL,6),
(4,3,'令人血压升高的短视频小游戏','http://localhost:9090/file/download/e40404652121455cb11d8919430e571c.png','<p style=\"text-align: start;\">令人血压升高的短视频小游戏</p>','http://localhost:9090/file/download/401843c5fe3f42a18f9fa7be55ee4803.mp4',23,'2024-04-01 08:13:06','审核通过',NULL,42);

/*Table structure for table `vediotags` */

DROP TABLE IF EXISTS `vediotags`;

CREATE TABLE `vediotags` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `vedio_id` int(11) DEFAULT NULL COMMENT '视频',
  `tags_id` int(11) DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='视频标签关联';

/*Data for the table `vediotags` */

insert  into `vediotags`(`id`,`vedio_id`,`tags_id`) values 
(4,4,3),
(3,4,6),
(5,3,2),
(6,3,1),
(7,3,5),
(8,2,7),
(9,1,5),
(10,1,6);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
