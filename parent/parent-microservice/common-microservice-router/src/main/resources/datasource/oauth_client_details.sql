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
-- Table structure for oauth_client_details
-- 表说明 http://andaily.com/spring-oauth-server/db_table_description.html
-- 表scheme https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
-- ----------------------------
CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);


