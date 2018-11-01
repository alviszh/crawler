/*
Navicat PGSQL Data Transfer

Source Server         : Test-crawler-10.167.202.177
Source Server Version : 90504
Source Host           : 10.167.202.177:5432
Source Database       : oauth2
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90504
File Encoding         : 65001

Date: 2018-08-24 19:47:37
*/

-- ----------------------------
-- Records of oauth_client_details
-- 表说明 http://andaily.com/spring-oauth-server/db_table_description.html
-- ----------------------------

-- ----------------------------
-- Records of oauth_client_details (client_secret is  123456)
-- ----------------------------
INSERT INTO "public"."oauth_client_details" VALUES ('client_id1', 'mobile,pbccrc', '{bcrypt}$2a$10$Fo5.udwnOG/XD0wgSVJOPOH3kOcir/Mb.fcYtqNNhhWS8gxG5lSvu', 'read,write', 'password,authorization_code,refresh_token,client_credentials', null, 'AUTH_MOBILE,AUTH_PBCCRC', '120', '5184000', null, null);
INSERT INTO "public"."oauth_client_details" VALUES ('client_id2', 'mobile', '{bcrypt}$2a$10$Fo5.udwnOG/XD0wgSVJOPOH3kOcir/Mb.fcYtqNNhhWS8gxG5lSvu', 'read,write', 'password,authorization_code,refresh_token,client_credentials', null, 'AUTH_MOBILE', '120', '5184000', null, null);
