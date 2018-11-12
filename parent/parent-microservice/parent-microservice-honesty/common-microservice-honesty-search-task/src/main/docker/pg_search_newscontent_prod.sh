#!/bin/sh
bin=/elasticsearch-jdbc-2.3.4.0/bin
lib=/elasticsearch-jdbc-2.3.4.0/lib
#sqlname=$1  
#index=$2
#indextype=$3

echo '
{
    "type" : "jdbc",
     "jdbc" : {
        "url" : "jdbc:mysql://mysql-mysql-lb-1:3306/sanwang_test",
       "user" : "root",
        "password" : "12qwaszx",
        "schedule" : "0/10 * 0-23 ? * *",
        "sql" : [
           {
                "statement" : "select a.id as _id ,a.*,b.id as \"newsListJsonEs.id\",b.abstract_txt as  \"newsListJsonEs.abstract_txt\",b.createtime as  \"newsListJsonEs.createtime\",b.keyword as  \"newsListJsonEs.key\",b.link_url as  \"newsListJsonEs.link_url\",b.taskid as  \"newsListJsonEs.taskid\",b.title as  \"newsListJsonEs.title\",b.type as  \"newsListJsonEs.type\"  from search_content a  join search_newslist b on a.news_list_json_id=b.id order by b.createtime desc limit 100"
          }
        ],
        "elasticsearch.autodiscover":true,
        "elasticsearch.cluster":"es-sanwang-prod",
        "index" : "newscontenttests",
        "type" : "newscontenttests",
        "elasticsearch" : {
           "host" : "172.31.17.29",
           "port" : 9300
        },
        "index_settings" : {
            "analysis" : {
            "analyzer" : {
                "ik" : {
                    "tokenizer" : "ik"
                }
            }
        }
        },
        "type_mapping": {
            "newscontent" : {
                "properties" : {
                    "id" : {
                        "type" : "integer",
                        "index" : "not_analyzed"
                    },
                    "subject" : {
                        "type" : "string",
                        "analyzer" : "ik"
                    },
                    "author" : {
                        "type" : "string",
                        "analyzer" : "ik"
                    },
                   "create_time" : {
                        "type" : "date"
                    },
                    "update_time" : {
                        "type" : "date"
                    }
               }
            }
        }
    }
}
' | java \
    -cp "${lib}/*" \
    -Dlog4j.configurationFile=${bin}/log4j2.xml \
    org.xbib.tools.Runner \
    org.xbib.tools.JDBCImporter
