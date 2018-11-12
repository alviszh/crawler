#!/bin/sh
bin=/elasticsearch-jdbc-2.3.4.0/bin
lib=/elasticsearch-jdbc-2.3.4.0/lib
sqlname=$1  
index=$2
<<<<<<< HEAD
esip=$3
=======
indextype=$2
#sqlname=$1  
#index=$2
#indextype=$3
>>>>>>> refs/remotes/origin/master

echo '
{
    "type" : "jdbc",
     "jdbc" : {
<<<<<<< HEAD
        "url" : "jdbc:mysql://mysql-mysql-lb-1:3306/'$sqlname'",
=======
        "url" : "jdbc:mysql://mysql-mysql-lb-1:3306/${sqlname}",
        "url" : "jdbc:mysql://mysql-mysql-lb-1:3306/$1",
>>>>>>> refs/remotes/origin/master
       "user" : "root",
        "password" : "12qwaszx",
        "schedule" : "0/10 * 0-23 ? * *",
        "sql" : [
           {
                "statement" : "select a.id as _id ,a.*,b.id as \"newsListJsonEs.id\",b.abstract_txt as 
                 \"newsListJsonEs.abstract_txt\",b.createtime as  \"newsListJsonEs.createtime\",b.keyword as  
                 \"newsListJsonEs.key\",b.link_url as  \"newsListJsonEs.link_url\",b.taskid as  
                 \"newsListJsonEs.taskid\",b.title as  \"newsListJsonEs.title\",b.type as  \"newsListJsonEs.type\,b.sensitivekey as  \"newsListJsonEs.sensitivekey\"  
                 from search_content a  join search_newslist b on a.news_list_json_id=b.id order by b.createtime desc limit 100"
          }
        ],
        "elasticsearch.autodiscover":true,
        "elasticsearch.cluster":"es-sanwang-prod",
<<<<<<< HEAD
        "index" : "'$index'",
        "type" :  "'$index'",
=======
        "index" : "${index}",
        "type" : "${indextype}" ,
        "index" : "$2",
        "type" : "$3",
>>>>>>> refs/remotes/origin/master
        "elasticsearch" : {
           "host" : "'$esip'",
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
            "'$index'" : {
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
