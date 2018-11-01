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

Date: 2018-09-10 14:23:19
*/


-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_access_token";
CREATE TABLE "public"."oauth_access_token" (
"token_id" varchar(256) COLLATE "default",
"token" bytea,
"authentication_id" varchar(256) COLLATE "default" NOT NULL,
"user_name" varchar(256) COLLATE "default",
"client_id" varchar(256) COLLATE "default",
"authentication" bytea,
"refresh_token" varchar(256) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table oauth_access_token
-- ----------------------------
ALTER TABLE "public"."oauth_access_token" ADD PRIMARY KEY ("authentication_id");
