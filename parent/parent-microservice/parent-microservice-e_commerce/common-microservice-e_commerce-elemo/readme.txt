登录地址：https://h5.ele.me/login/#redirect=https%3A%2F%2Fwww.ele.me%2Fhome%2F



{
    "code": "0",状态码
    "message": "请求用户数据成功",状态描述
    "task_id": "fff11ca0-4301-11e8-94ff-acde48000000",任务唯一id
    "data": {
基本信息   "user_info": {
            "userName": "卢****8",账户名
            "userId": "643000",账户id
            "balance": "0",余额
            "mobile": "13758220000",号码
            "point": "500"金币个数
        },
收货地址   "address_list": [
            {
                "name": "张三",收货人姓名
                "mobile": "13758220000",收货人电话
                "address_id": "13867000",收货人地址Id
                "address": "中国移动浙江工业大学之江学院店",收货人地址
                "address_detail": "学6",收货人详细地址
                "tag": "",标签
                "is_valid": "1"是否已校验   1-已校验；0-未校验
            },
            {...}
        ],
 收藏信息   "favor_restaurant_list": [
            {
                "name": "有态度爆炒料理(城西店)",店铺名称
                "address": "浙江省杭州市西湖区余杭塘路xxx号2-xxA/xxB",店铺地址
                "description": "有态度爆炒料理是一家专注于麻辣爆炒的互联网餐饮有限公司，不管是口味，包装到用餐体验都称得上是外卖中的行业标杆，受到食客的高度追捧。",店铺描述
                "restaurant_id": "156050000",店铺Id
                "mobile": "151600004220571-85370000"店铺联系电话
            },
            {...}
        ],
 订单信息   "order_list": [
            {
                "order_id": "3021503734102110229",订单Id
                "order_status": "订单已完成",订单状态
                "contact_address": "龙港爱绿幼儿园通港路分园宫后路号（繁荣路宫后路十字路口）",收货地址
                "contact_name": "卢(先生)",联系人
                "contact_mobile": "13758220000",电话
                "order_create_time": "2018-04-05 21:59:06",订单创建时间
                "restaurant_name": "酸菜鸡壳(龙湖店)",店铺名称
                "restaurant_id": "156381881",店铺Id
                "total_amount": "44",总价
                "delivery_company": "饿了么",配送服务公司
                "rider_name": "张三",骑手名字
                "rider_page_url": "",骑手信息url
                "rider_phone": "13695551111",骑手电话
  订单详情      "order_detail_list": [
                    {
                        "price": "17",单价
                        "quantity": "2",数量
                        "name": "酸菜鸡壳十粉干"名称
                    },
                    {
                        "price": "5",
                        "quantity": "2",
                        "name": "百事可乐"
                    }
                ]
            },
            {...}
        ]
    }
}



//个人信息
		String user_info = "{\"avatar\":\"bf3796d02f7b4cac20d079fcecad20c6jpeg\",\"balance\":0,\"brand_member_new\":0,\"column_desc\":{\"game_desc\":\"玩游戏领红包\",\"game_image_hash\":\"05f108ca4e0c543488799f0c7c708cb1jpeg\",\"game_is_show\":1,\"game_link\":\"https://gamecenter.faas.ele.me\",\"gift_mall_desc\":\"0元好物在这里\"},\"current_address_id\":0,\"current_invoice_id\":0,\"delivery_card_expire_days\":0,\"email\":\"\",\"gift_amount\":2,\"id\":968960002,\"is_active\":1,\"is_email_valid\":false,\"is_mobile_valid\":true,\"mobile\":\"13552959225\",\"point\":8,\"real_point\":0,\"supervip_status\":1,\"user_id\":969500818,\"username\":\"4e19873692c\"}";
		
		JSONObject json = JSONObject.fromObject(user_info);
		
		//账户名
		String username = json.getString("username");
		System.out.println("账户名-----"+username);
		//账户Id
		String user_id = json.getString("user_id");
		System.out.println("账户Id-----"+user_id);
		//余额
		String balance = json.getString("balance");
		System.out.println("余额-----"+balance);
		//号码
		String mobile = json.getString("mobile");
		System.out.println("号码-----"+mobile);
		//金币个数
		String point = json.getString("point");
		System.out.println("金币个数-----"+point);

//收货地址
		String address_list = "[{\"address\":\"北京征医道中医药研究院\",\"address_detail\":\"汉威国际广场3区3号楼7楼\",\"city_id\":3,\"city_name\":\"\",\"created_at\":1530870127,\"district_id\":5004,\"entry_id\":3,\"geohash\":0,\"id\":211868979974,\"is_valid\":1,\"name\":\"齐忠斌\",\"phone\":\"13552959225\",\"phone_bk\":\"\",\"poi_type\":0,\"sex\":1,\"st_geohash\":\"wx4dw3e6k3ng\",\"tag\":\"公司\",\"tag_type\":3,\"user_id\":969500818},{\"address\":\"清华同方科技大厦D座-西楼\",\"address_detail\":\"王庄路1号院附近\",\"city_id\":3,\"city_name\":\"\",\"created_at\":1528527158,\"district_id\":5006,\"entry_id\":3,\"geohash\":0,\"id\":196254757638,\"is_valid\":1,\"name\":\"齐忠斌\",\"phone\":\"13552959225\",\"phone_bk\":\"\",\"poi_type\":0,\"sex\":1,\"st_geohash\":\"wx4ex3khev9z\",\"tag\":\"公司\",\"tag_type\":3,\"user_id\":969500818},{\"address\":\"天骄俊园南区\",\"address_detail\":\"天骄俊园 3号楼2单元602\",\"city_id\":3,\"city_name\":\"\",\"created_at\":1522068436,\"district_id\":5008,\"entry_id\":1,\"geohash\":0,\"id\":2194955153,\"is_valid\":1,\"name\":\"齐忠斌\",\"phone\":\"13552959225\",\"phone_bk\":\"\",\"poi_type\":0,\"sex\":1,\"st_geohash\":\"wx4d5sm02c8e\",\"tag\":\"家\",\"tag_type\":1,\"user_id\":969500818},{\"address\":\"住总·旗胜家园-北区\",\"address_detail\":\"建材城西路 9号楼1101\",\"city_id\":3,\"city_name\":\"\",\"created_at\":1513943100,\"district_id\":5011,\"entry_id\":1,\"geohash\":0,\"id\":1678313113,\"is_valid\":1,\"name\":\"齐忠斌\",\"phone\":\"13552959225\",\"phone_bk\":\"\",\"poi_type\":0,\"sex\":1,\"st_geohash\":\"wx4ezy1z01h1\",\"tag\":\"家\",\"tag_type\":1,\"user_id\":969500818}]";
		JSONArray array = JSONArray.fromObject(address_list);
		for (int i = 0; i < array.size(); i++) {
			Object object = array.get(i);
			JSONObject json = JSONObject.fromObject(object);
			int j =  i + 1;
			System.out.println("===========================总共"+array.size()+"条记录------"+"目前是第("+j+")条记录===========================");
			//收货人姓名
			String name = json.getString("name");
			System.out.println("收货人姓名-----"+name);
			//收货人电话
			String phone = json.getString("phone");
			System.out.println("收货人电话-----"+phone);
			//收货人地址Id
			String address_id = json.getString("id");
			System.out.println("收货人地址Id-----"+address_id);
			//收货人地址
			String address = json.getString("address");
			System.out.println("收货人地址-----"+address);
			//收货人详细地址
			String address_detail = json.getString("address_detail");
			System.out.println("收货人详细地址-----"+address_detail);
			//标签
			String tag = json.getString("tag");
			System.out.println("标签-----"+tag);
			//是否已校验   1-已校验；0-未校验
			String is_valid = json.getString("is_valid");
			System.out.println("是否已校验   1-已校验；0-未校验-----"+is_valid);
		}
