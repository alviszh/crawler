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

Date: 2018-09-10 14:23:26
*/


-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_refresh_token";
CREATE TABLE "public"."oauth_refresh_token" (
"token_id" varchar(256) COLLATE "default",
"token" bytea,
"authentication" bytea
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
