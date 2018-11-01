'use strict';;'use strict';

/*
 parameters
 <Header title={} title_img={} height={} background={} />
 */

var Header = React.createClass({
    displayName: 'Header',

    propTypes: {
        show_back_btn: React.PropTypes.bool,
        title_img: React.PropTypes.string,
        background: React.PropTypes.string,
        title: React.PropTypes.string.isRequired
    },
    getDefaultProps: function getDefaultProps() {
        return {};
    },
    getInitialState: function getInitialState() {
        return {
            background: this.props.background || 'white',
            title: this.props.title,
            title_img: this.props.title_img,
            show_back_btn: this.props.show_back_btn !== false
        };
    },
    backClickHandler: function backClickHandler() {
        if (this.props.back_handler) {
            this.props.back_handler();
        } else {
            history.go(-1);
            //App里面后退不起作用 判断在App环境当中关掉当前webview
            setTimeout(function () {
                return NativeBridge.isReady && NativeBridge.close();
            }, 300);
        }
    },
    render: function render() {
        var _state = this.state,
            title = _state.title,
            background = _state.background,
            title_img = _state.title_img,
            height = 100,
            lineHeight = 100;

        var IOS_DELTA = 22;

        // compatible with iPhone state bar, move down 22px
        var inIOSAPP = false;
        if ($FW.Browser.inIOSApp()) {
            // hack, 商品详情页面不做iOS的手机状态栏高度兼容
            if (location.href.indexOf('/static/mall/product-detail/') < 0) {
                height += IOS_DELTA;
                lineHeight += IOS_DELTA * 2.4;
                inIOSAPP = true;
            }
        }

        var fontSize = title && title.length > 7 ? '36px' : '32px';

        var _style_header_fixed = {
            transform: 'translate3d(0, 0, 0)',
            position: "fixed",
            top: "0px",
            left: "0px",
            right: "0px",
            height: height + 'px',
            textAlign: "center",
            lineHeight: lineHeight + 'px',
            background: background,
            zIndex: '9',
            textShadow: "0 0 8px white",
            fontSize: fontSize,
            borderBottom: "1px solid #d8d8d8",
            color: "#333"
        };

        var _style_header_arrow = {
            display: "block",
            position: "absolute",
            width: height + "px",
            height: height + "px",
            lineHeight: height + "px",
            fontFamily: "serif",
            fontSize: fontSize,
            fontWeight: 'bold',
            color: "#536f95",
            overflow: "hidden",
            left: "0px",
            top: "0px",
            backgroundImage: "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RTFDQkQyMDM5RjUzMTFFNkJFNEE5MTVDMEU3MEVDMjkiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RTFDQkQyMDQ5RjUzMTFFNkJFNEE5MTVDMEU3MEVDMjkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpFMUNCRDIwMTlGNTMxMUU2QkU0QTkxNUMwRTcwRUMyOSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpFMUNCRDIwMjlGNTMxMUU2QkU0QTkxNUMwRTcwRUMyOSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pj+ASv8AAAG1SURBVHja7NvBKkRhFMBxM2GjrL0BHoCylBdQbHgAC1l4FKVs7NmMrEUsFQ9AnsBadhbjqFGTWEm+c+7vX6fpbqY785s7d+bON73hcDihduoBASIgQAQEiIAAERAgngUgAgJEQIAICBABASIgAgJEQIAICBABASIgAgLkN23uH/3ZfQ8Odpt8zP2OvPBmYtZGt03XBZC5mLuYq5iL1nd2sgMY1zGLo+15R0g7GG8xO0DawdiOOQfSDsYgw873YQCB0QGQEhhVQMpgVAAphZEdpBxG5m/q32FsxZxlf//twwACowhIeYxMIJ3AyHJSn425iVmojpHlCFkaw/josCpGFpD7mMex7b2YDSD/10vM6hjKdMxJVZQsJ/XnrqBk+tjbCZRsXwzLo2S8dFIaJevV3rIomS+/l0TJ/gNVOZQKP+GWQqmyyOE7lNOMKJWWAX1FmcqIUm2hXHqUiktJU6NUXWz9E8o6kLZQjoG0gfIw2n5qfYer/4PqE2U5ZiXmFkgbvcZcZthR/1MHIiBABASIgAARECBAgAgIEAEBIiBABASIgAgIEAEBIiBABASIgAhI+70LMACHPyRoayPenAAAAABJRU5ErkJggg==')",
            backgroundRepeat: "no-repeat"
        };

        var _img_style = {
            display: 'block',
            margin: '0 auto',
            width: "182px",
            position: "relative",
            top: "30px"
        };

        if (inIOSAPP) {
            _img_style.top = 30 + IOS_DELTA + 'px';
            _style_header_arrow.top = IOS_DELTA + 'px';
        }

        if (title_img) title = React.createElement('img', { src: title_img, style: _img_style });

        var back_btn = React.createElement(
            'div',
            { className: '_style_header_arrow', style: _style_header_arrow,
                onClick: this.backClickHandler },
            ' '
        );

        return React.createElement(
            'div',
            { style: { height: height + 'px' } },
            React.createElement(
                'div',
                { className: '_style_header_fixed', style: _style_header_fixed },
                this.state.show_back_btn && back_btn,
                title
            )
        );
    }
});;'use strict';

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var BottomNavBar = React.createClass({
    displayName: 'BottomNavBar',

    getInitialState: function getInitialState() {
        var height = parseInt(this.props.height) || 100;
        var lineHeight = parseInt(this.props.height) || 100;

        var inIOS = navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;
        var inApp = $FW.Browser.inApp();

        if (inIOS && inApp) {
            height += 22;
            lineHeight = 152;
        }

        var match_url = function match_url(w) {
            return location.pathname.indexOf('/static/mall/' + w) >= 0;
        };

        var tab = {
            home: location.pathname === '/' || match_url('home'),
            category: match_url('product'),
            cart: match_url('cart'),
            user: match_url('user')
        };

        return {
            height: height,
            lineHeight: lineHeight,
            tab: tab,
            background: this.props.background || 'white'
        };
    },

    exitHandler: function exitHandler() {
        $FW.Browser.inApp() ? NativeBridge.toNative('app_back_native') : location.href = 'https://m.9888.cn';
    },

    render: function render() {
        var _back_to_index2;

        var inApp = $FW.Browser.inApp();
        var posAW = inApp ? "absolute" : "static";
        var Top = inApp ? "15px" : "0";
        var pTopAW = inApp ? "0" : "15px";
        var disAW = inApp ? "block" : "none";
        var widAW = inApp ? "100px" : "25%";

        var tab = this.state.tab;


        var _style_footer_fixed = {
            // background: "url(images/global-bottom-nav-bg.png) no-repeat",
            borderTop: '1px solid #d8d8d8',
            background: 'white',
            width: "720px",
            height: "100px",
            fontSize: '22px',
            position: "fixed",
            left: "0",
            right: "0",
            bottom: "0"
        };

        var _style_footer_item_base = {
            backgroundRepeat: 'no-repeat',
            display: "inline-block",
            width: widAW,
            color: '#ef3837',
            textAlign: 'center',
            top: Top,
            paddingTop: pTopAW,
            position: posAW
        };

        var gray_filter = {
            filter: 'grayscale(100%)',
            WebkitFilter: 'grayscale(100%)'
        };
        var _style_footer_item_home = Object.assign({
            height: "80px",
            left: "28px"
        }, _style_footer_item_base, tab.home ? {} : gray_filter);

        var _style_footer_item_category = Object.assign({
            width: "42px",
            height: "70px",
            left: "160px"
        }, _style_footer_item_base, tab.category ? {} : gray_filter);

        var _style_footer_item_cart = Object.assign({
            width: "65px",
            height: "70px",
            right: "160px"
        }, _style_footer_item_base, tab.cart ? {} : gray_filter);

        var _style_footer_item_user = Object.assign({
            width: "87px",
            height: "70px",
            right: "28px"
        }, _style_footer_item_base, tab.user ? {} : gray_filter);

        var _back_to_index = (_back_to_index2 = {
            display: disAW,
            width: "96px",
            height: "96px",
            padding: '10px',
            position: "absolute",
            top: "31px",
            left: "50%",
            transform: "translateX(-50%)",
            WebkitTransform: "translateX(-50%)",
            background: 'white'
        }, _defineProperty(_back_to_index2, 'position', 'relative'), _defineProperty(_back_to_index2, 'top', '-22px'), _defineProperty(_back_to_index2, 'borderRadius', '50%'), _back_to_index2);

        var _back_to_index_icon = {
            width: '96px',
            height: '96px',
            borderRadius: '50%',
            background: '#ef3837 url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEEAAAA5CAMAAABeWLbbAAAAq1BMVEUAAAD////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Nr6iZAAAAOHRSTlMA9+7y+rfHOD+jdkQt0+TDM+ocXPy7Bd/Xqo5VIhDNlBWmC2u/soZQOycDcgeuxoB7aGFKKgG8mjV7IaIAAANeSURBVEjH7ZbbmppADIADigJSFUG0rCCyHnbVdY9t8/5P1iSzI0fb1a+X/S9gIsMPCWQEbiQ6uK5XhP0h1ul+h0/usYqpTqGRBZpJF4mRo/lucvwEikenyjPsXNdd0oy5694fgUnVCQWJKPs6fEvnBSuADqJh0ISQrmTuaYItd51DiSUSM30TfNgUQsSOGPqSBY/GNEOK8Axl9h0kAhBeabgEwWoa+jyB8KHKW1EzuP+LQV1ul3RLDAFmSGzrhmmbIUbiG0Qby7KkgLRfcvKEcdQGVGpsM/CvZgZCh6eWX4OVVNIqs20YgiB430PTANnAtj0gxnaFj7pBUxjq6CQYTq1uGBoFyJTinjbMims0DZteQciGUrw8G2LrjkpjvbDhpixmFob5AjFuN2wdZ9AwvDpOUDJMEF8cxEgZir5QhhPtX2uGFY0OJUOGmA7RSMRQ6k33EH12llEYzs20LxnmKKzFUKwPyWLBBjCki7RB30JaroNM8gFqBkpjw9FI5muDHhy+YKDQ0i9wGBUGOYzJFYYIiUPJsJF+/6phSIRcS9kT55iwWg0WHfFlivS9+SdGQMxM8wkgNc0plcw0U95ofGsP//mXRN6gwUN2xfnbDrYxfYSv8WODFzA/9ByXFqx1BJDYVR6AucOLTLXhG0cTAAdrTFQnXyRMaoZdT6MS7xx1b/vfFZvQGH0OUyQ+qoaCXyrNDLRhBUK0fjo/gQckvHbDfq0FdYObRtAwTBzHUR81TgxCPheBn0Bh6N0J84CfjS3jrTbs8EwXmImvBWdDwZjiuAjFkB9cV87Zum8LwjZUCh4HSd3g6ZyZZh022ODndQbPrSD/w9cZiHFMADzT1jZuMcjIhwHHg5sMmfru63Ep4SbDL36mUR7SLr7N0JHyx9wz+U2GnfrRpO0J2gyz/C8Gbrch2Bw9VA2nAWMnFB47gQQvLYaEdy6c5HlUDSs4c59e7s2l1PHIddxeMkQH37tokPwt2HIdjyUD97o5Vax9nH8OvzVWmOydt4/AbbYGbbhmlZM6ztTNjbXB/PNKawGxOPV63EMjeY/eYC2ed1orkYh5RXm6JPAXUKwwveiZtwBk646pqIKjqvfit6awWajDAbHLAPIg6FM8CO5ygCwg3o/nlfNx3MBL4Gv8BiXsRv1VM+PXAAAAAElFTkSuQmCC") no-repeat center center'
        };

        var _image_icon_style = {
            width: '44px',
            height: '44px',
            display: 'block',
            margin: '0 auto 6px'
        };

        var _image_icon_back = {
            background: 'red'
        };

        return React.createElement(
            'div',
            { className: '_style_footer_fixed', style: _style_footer_fixed },
            React.createElement(
                'a',
                { style: _style_footer_item_home, href: tab.home ? '' : "/" },
                React.createElement('img', { style: _image_icon_style,
                    src: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAsCAYAAAAehFoBAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUI0QzFGMjVGMjhDMTFFNkIzM0VCQjBDMUE4ODkyNEQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUI0QzFGMjZGMjhDMTFFNkIzM0VCQjBDMUE4ODkyNEQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo5QjRDMUYyM0YyOEMxMUU2QjMzRUJCMEMxQTg4OTI0RCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo5QjRDMUYyNEYyOEMxMUU2QjMzRUJCMEMxQTg4OTI0RCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PtOho88AAANoSURBVHja7Jl9aE1xGMfP2bXrZa5JEiNthpKbvEctb8N0mSImUtSyltC81MxS6yp5zSKW8lJEWYjUZggxtkj+YFqoUVoWeV3Nnbi+T75nHadzjl06557Vfeqz596d3+7ve5/7O8/LnRqNRpWuZElKF7MuJ7ib/Pg0dYrdmmFgGwiBQQ5o+A6eg5PgEGi3Wti3rv63YBsLgruy1sGgJYPRYB+YB+bbibY7En5wimKvgJFAdQA/hTaD2WDzv55h+XjGgZcgD7xwKMJyJGrAaiApaweYFqvgVaAAtFHsNxfup+tgF/CBcyCts4LHgiN8vA48djEJbAc3wECKTv6b4FRwHvQCx8AJl7PWD7ACvAFZYI+dYLkBToNM8Aisj1OqfQeWMlMU8bGp4BKQCz6AJS6dWyurB5v4+DgYZRQ8GITBT7ASvPJAUTsMzoAAKDcKzubdWQWqPVSJ1zLtzWRQOwRPpL/vsdbhC7jEbLFVL3gh/VUP9jt76XP1gofQP7P5Qzkyk8CATmwyQveadiaVdAHoYbPmCX2aXnAz/XCbBuU2eMAbcobNBuXsvl6zWlpZPtOn9Cm3tM7RxDLp3+oFazdamPnYaCVM5FKqe4KjoI/JujlgA1OivPZ+RttoQ9mdyV4fgfS3W0zWqdTUoVETLDW8FSwGF8AERjUDHABlbEzy+BFJ53YPzAXdQX8KvcxNpH8+C3qDWrCcbzCFe2gtazX7FrGdrGzp3Fs0XOT6VmpUVJnp2MBLdCotel8pmRvZwckLXrOInMJyXsDyLnf4LJviEGKEixhxn0WmkEDVSAOfZOiWxoCDoIn5r4VvYjLFKjzD40Epox0Bn9m0LOLZlDf4lZ/AGnAHvOeResiyP51itXOfxb1auHcTtQTZfirGCOstqjtD8TDT/SXCqn7M1wn3jGARmRjzE4ITgs1NqlcFS3KEvoK/95zgHPAUFFKgn76Qv8/xkuAMTrgBJv4gy3WQzwO8nu4VwcW6aXsZaOAA2cDnlbxe7BXBIfoyi+thw7q4C9a+oWm0uN6oG3Q9Idin6+isvhxRLDqxRB5OCHZKcBt9ahw0pdC3xyJYa0JliOznolgZz3YbNPxhVqO1fE97k+NOfoxN9/+u0aJbGkuEZSLOpo+4GGHZq44Dca3ZAjXxn1CH7ZcAAwC4hMgDcaPlXgAAAABJRU5ErkJggg==' }),
                '\u9996\u9875'
            ),
            React.createElement(
                'a',
                { className: '_style_footer_item_category', style: _style_footer_item_category,
                    href: tab.category ? '' : "/static/mall/product-category/index.html" },
                React.createElement('img', { style: _image_icon_style,
                    src: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAsCAYAAAAehFoBAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUI0QzFGMjFGMjhDMTFFNkIzM0VCQjBDMUE4ODkyNEQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUI0QzFGMjJGMjhDMTFFNkIzM0VCQjBDMUE4ODkyNEQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo5QjRDMUYxRkYyOEMxMUU2QjMzRUJCMEMxQTg4OTI0RCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo5QjRDMUYyMEYyOEMxMUU2QjMzRUJCMEMxQTg4OTI0RCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PhnlT34AAAHsSURBVHja7JjNSwJBGMZdszqEEdjXJYrtGnRQoiCpQwcJOnXIvqCb+A/UtUuHOnS2c2AKnQwCzwZGFBHdgig69EVBJkUEtT0LjyCy6m647ggz8ONl33lmfXYc351R0TTN1UhNkYalYWnYYcNvY6MZhHFB/B11ZI+DxQm36BNamvAYiIIiP4Hb1WBNGpaGpWFpWAzD+ufMgxR4Bhpjinm3SIZVcALiYAZ0Md/F6zj7VREM6yaywA/uQBT0g1bGKPN+6lQnDev3ToJukAZDIEaD34wx5tPUJat5stNwGATADZgD+TK6PPtvqQ9bNZzhj8IKGYP7LDNuglyVh8tRVzzO1hlWDHIBxkOT9yjohiuJ7NxedjI+mdQ/MvqcWsMvjD0m9b2Mr04ZPmWcNqkv6C6cMrzLuAraqmjbwVrJuLobTnCW9ZfBPvCW0XlZfweoTzhl+Jc1Vd8zhMAliIA+0MIYYT5EXZjjLB3za218kDPor6A54+S9cy1/FjpwzK/75ucajIAFcADu+Wp+4PUi+5vBBNixWoftWh57pFyb5a5tCZyDbdE38Ff8Fn7AFphqhBOH/npeB02sFmojHJE2WAZ9RiXOI6BhvWytsOx92bW9rMV2tLh9AL2+ToqyJJR/D5T/wEvD0rA0XLH9CTAAmRipa3KpD60AAAAASUVORK5CYII=' }),
                '\u54C1\u7C7B'
            ),
            React.createElement(
                'a',
                { className: '_back_to_index', style: _back_to_index, onClick: this.exitHandler },
                React.createElement('div', { className: '_back_to_index_icon', style: _back_to_index_icon })
            ),
            React.createElement(
                'a',
                { className: '_style_footer_item_cart', style: _style_footer_item_cart,
                    href: tab.cart ? '' : "/static/mall/cart/index.html" },
                React.createElement('img', { style: _image_icon_style,
                    src: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAsCAYAAAAehFoBAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MUZCRUExNTJGMjhBMTFFNkIzM0VCQjBDMUE4ODkyNEQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUI0QzFGMUVGMjhDMTFFNkIzM0VCQjBDMUE4ODkyNEQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoxRkJFQTE1MEYyOEExMUU2QjMzRUJCMEMxQTg4OTI0RCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoxRkJFQTE1MUYyOEExMUU2QjMzRUJCMEMxQTg4OTI0RCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PpLgeW0AAALdSURBVHja7JlNSFVBFMfvLSvCKBGpyKVZaWG1U6hFVJpCq5AybVEtciFFuAlqU2FfiwSrnQtB0T6oXVhg0Rdk0EItsoyiXRAIipbEo17/Q/8Lw+Dr3rlPZ96FBn6cO+/yzvtzOHPmzDw/nU57SRoLvISNvOBhoqryGcx25d0MuAmOgZT+xYKXg84jvFh7txQcBidyNSUqga/QwM+rk5LDr2nLkyL4M5gCxaAo5xbdLOM3eAuqQAV4rL7EIrUuVhZ6WFkbon0E0g4YB1K9jkaJsIxhxxlQyFIrrAQXwyI8okTat8xCsAo0U0NzlJ3uDXO5DCyyHF353W+gX4l2qOBpVoslYIOjtNhJ+yJqLxEsvC2OBNfS9kcVHORxhQOxkse7+fwgquCgUmx2IFj2gALwCYwlIcK1anSjCv4CJlliVlsWvEfNX5MGfshBWkhwtoKfalsQVbCLtKjhBvKUhwkjwcMOSludng5xImwrJaSc7dIXnIlgaTN/gfXc9WyUs0Lusu/jCJ5hHZTubqMFwcGx7GE2x3ybC28v7f1sBNvqKWr4GxPgSS5HeAVoArc4vwK+m5zpglEKLilNyA4eX+Zz3KFgz1SwVIVBNiA2GvZXoBN0cW4s+ALFSi08Ar66Pub7mW4veYyXpmc5WJMLYuWYn+clYKgXj2FVYoC200FraXxVJeM062EdU8L2RcpzU8Gyj0sy3/P+3rNZX2NxNo4PYB8XX3DJcRCMsvSMch7nsqRB83NIe78tzsahj0bQo8zlkqWXP9Br4Kce9Gl+uvncY1zWMpQ5Ge/o/BS4Bo7LnRcjVK6v6n/4klulTeAMaAct4DLTsCyTnzh/ypTS3gA/wHXOSwz9rKW9Sj8dnK/LpkrMNj7SSmSX0aqfm/ppBfngJOdjcy34PG0bK0cb5+cM/ZxV/E2zDVD9z5ngPq5uybUUc/cAuG3o5y7Yz+NXiv4awxau//+f0HkefwQYAO6f2ih8185sAAAAAElFTkSuQmCC' }),
                '\u8D2D\u7269\u8F66'
            ),
            React.createElement(
                'a',
                { className: '_style_footer_item_user', style: _style_footer_item_user,
                    href: tab.user ? '' : "/static/mall/user/index.html" },
                React.createElement('img', { style: _image_icon_style,
                    src: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAsCAYAAAAehFoBAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MUZCRUExNEVGMjhBMTFFNkIzM0VCQjBDMUE4ODkyNEQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MUZCRUExNEZGMjhBMTFFNkIzM0VCQjBDMUE4ODkyNEQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoxRkJFQTE0Q0YyOEExMUU2QjMzRUJCMEMxQTg4OTI0RCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoxRkJFQTE0REYyOEExMUU2QjMzRUJCMEMxQTg4OTI0RCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PsU9wKUAAAJHSURBVHja7JhNKDVRGMfnujfehCx97XhLFmKFHYlcC8pC3qWXUJJYUZKPYktZet93aY1SrmKjrh1ZSend+Nr5uEmSxv/kPzrGDDP3uucO5qlfZ+7c88z5nzPPOc85E9B1XftMFvAF+4LfEHxZUy3/VwdmQQXIUKTnDuyBUbAlbuRGd15USLNxbAAboEqhWI1tVbHtBqsKdoKnQBAsgCzxJhQh2ppn21NuQkK8mnSQA2KKwzQbXFPDD6chkc4yloJ5FZPCw3FIeNa+heAwOCVh1f52k864GbDwEQ3l8/oMFLhs04n/c/tOJ53jDqv2j0dwtzQCA3H4D0uj2KVC8BrY5/X/OPwNn30+S8kqsS3tN9xarekZSgSvsuxiGnVqQYaU/AwlgiN8paVgxIWfqFtC34hKwWLCDIEHMAkGHfgMsu4DfXWVgoVtgl5ez4Eo+AWKQIgU8V6UdYT10TcuCyW4jv4Bx+AfqCZ2JpJEJ1hPpMFEBWsUUMyRbAWVII//nYNdsAyWwG2yjkj6B2UyLYE5kpTU7G8vfcG+4K8u+HlZM3318cyyZtwwljerEW7y0IC2vBcSokczHhI8bdZoFtzO1CryflYKhWaCE1AOOuwEh9gjo2c3KRR8K2mZlPc8suDf4Cc4AoseCIe/1FJCba8E97OcAPceEHxPLcJ6rASXSqdir5hxjCqzEnzAcgwUpjo/UMM4fx9aJY5mFCsuT8EqTJwB25A4VswjLEKhXnv6XH/lAaEXDIlGDuTrE8en2kv4gpNkjwIMAJL6nASKPFTbAAAAAElFTkSuQmCC' }),
                '\u6211\u7684\u5546\u57CE'
            )
        );
    }
});;'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

/*
 parameters
 <Loading />
 */

var GlobalLoading = function (_React$Component) {
    _inherits(GlobalLoading, _React$Component);

    function GlobalLoading() {
        _classCallCheck(this, GlobalLoading);

        return _possibleConstructorReturn(this, (GlobalLoading.__proto__ || Object.getPrototypeOf(GlobalLoading)).apply(this, arguments));
    }

    _createClass(GlobalLoading, [{
        key: 'componentWillUnmount',
        value: function componentWillUnmount() {
            this.props.unMountHandler && this.props.unMountHandler();
        }
    }, {
        key: 'render',
        value: function render() {
            var theme = this.props.theme;

            var cn = theme == 'mini' ? 'global-ajax-loading-2' : 'global-ajax-loading';
            return React.createElement(
                'div',
                { className: cn },
                React.createElement('div', { className: 'loader-text' }),
                React.createElement('div', { className: 'loader-img' }),
                React.createElement('div', { className: 'loader' }),
                React.createElement(
                    'div',
                    { className: 'loading-info' },
                    '\u5168\u529B\u52A0\u8F7D\u4E2D'
                ),
                React.createElement('div', { className: 'bg' })
            );
        }
    }]);

    return GlobalLoading;
}(React.Component);

;;"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

/*
 parameters
 <Alert title={'这个是标题'} confirm_text={'CONFIRM'} show_callback={} hide_callback={} />

 title 显示标题
 header Show big header text
 confirm_text 是否显示确认按钮, 及其提示文字
 show_callback 显示之后的回调
 hide_callback 隐藏之后的回调
 */

var GlobalAlert = function (_React$Component) {
    _inherits(GlobalAlert, _React$Component);

    function GlobalAlert() {
        _classCallCheck(this, GlobalAlert);

        var _this = _possibleConstructorReturn(this, (GlobalAlert.__proto__ || Object.getPrototypeOf(GlobalAlert)).call(this));

        _this.hideHandler = function () {
            ReactDOM.unmountComponentAtNode(document.getElementById(_this.props.id));
        };

        _this.state = {
            show: true
        };
        return _this;
    }

    _createClass(GlobalAlert, [{
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
            this.props.unMountAlert && this.props.unMountAlert();
        }
    }, {
        key: "render",
        value: function render() {
            var fontSize = '40px';

            var style_pop = {
                position: "fixed",
                top: "0px",
                bottom: "0px",
                width: "100%",
                zIndex: '99',
                fontSize: fontSize
            };
            var style_bg = {
                position: "absolute",
                top: "0px",
                left: "0px",
                right: "0px",
                bottom: "0px",
                background: "black",
                opacity: "0.3"
            };
            var _alert_style_panel = {
                display: "table",
                margin: "50% auto 0",
                position: "relative",
                width: window.innerWidth * 0.8 + "px",
                borderRadius: "8px",
                background: "white"
            };
            var style_close = {
                display: "none",
                position: "absolute",
                top: "-20px",
                right: "-20px",
                width: "40px",
                height: "40px",
                fontSize: "35px",
                background: 'white',
                borderRadius: '50%',
                textAlign: 'center',
                lineHeight: '40px',
                border: '5px solid #aaa',
                fontWeight: 'bold'
            };
            var style_text = {
                textAlign: "left",
                margin: "30px auto",
                lineHeight: "40px",
                color: "#555555",
                padding: "0 36px",
                maxWidth: "576px",
                fontSize: "28px"
            };
            var style_confirm = {
                display: "block",
                float: "left",
                width: window.innerWidth * 0.3 + "px",
                textAlign: "center"
            };
            var style_cancel = {
                display: "block",
                float: "right",
                width: window.innerWidth * 0.3 + "px",
                textAlign: "center"
            };
            var style_one_big = {
                display: "block",
                width: "516px",
                height: "74px",
                lineHeight: "74px",
                textAlign: "center",
                color: "white",
                background: "#f9655a",
                borderRadius: '4px',
                margin: '0 auto 30px',
                fontSize: "34px"
            };
            var style_header = {
                width: "538px",
                height: "88px",
                fontSize: "32px",
                color: "#333333",
                lineHeight: "88px",
                borderBottom: "1px solid #d8d8d8",
                backgroundColor: "#eee",
                paddingLeft: "38px",
                borderTopLeftRadius: "8px",
                borderTopRightRadius: "8px"
            };
            var title_index = {
                width: "36px",
                display: "inline-block",
                float: "left"
            };
            var title_content = {
                width: "468px",
                display: "inline-block",
                float: "left"
            };
            var title_wrap = {
                clear: "both",
                overflow: "hidden"
            };

            if (!this.state.show) return null;

            var title = null;
            if (this.props.title instanceof Array) {
                title = React.createElement(
                    "div",
                    null,
                    this.props.title.map(function (i, index) {
                        return React.createElement(
                            "div",
                            { key: index,
                                style: title_wrap },
                            React.createElement(
                                "span",
                                {
                                    style: title_index },
                                index + 1,
                                "\u3001"
                            ),
                            " ",
                            React.createElement(
                                "span",
                                { style: title_content },
                                " ",
                                i,
                                " "
                            )
                        );
                    })
                );
            } else {
                title = this.props.title;
            }

            var header = null;
            if (this.props.header) {
                header = React.createElement(
                    "div",
                    { style: style_header },
                    this.props.header
                );
            }

            return React.createElement(
                "div",
                { style: style_pop },
                React.createElement("div", { style: style_bg, onClick: this.hideHandler }),
                React.createElement(
                    "div",
                    { className: "_alert_style_panel", style: _alert_style_panel },
                    React.createElement(
                        "div",
                        { style: style_close, onClick: this.hideHandler },
                        "\xD7"
                    ),
                    header,
                    React.createElement(
                        "div",
                        { style: style_text },
                        title
                    ),
                    this.props.confirm_text && !this.props.cancel_btn ? React.createElement(
                        "a",
                        { style: style_one_big, onClick: this.hideHandler },
                        this.props.confirm_text
                    ) : null,
                    this.props.confirm_btn ? React.createElement(
                        "a",
                        { style: style_confirm, onClick: this.hideHandler },
                        "CONFIRM"
                    ) : null,
                    this.props.cancel_btn ? React.createElement(
                        "a",
                        { style: style_cancel, onClick: this.hideHandler },
                        "CANCEL"
                    ) : null
                )
            );
        }
    }]);

    return GlobalAlert;
}(React.Component);

GlobalAlert.defaultProps = {
    title: '好像出了点问题!?'
};;'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

/*
 <BannerGroup />

 parameters

 */

var BannerGroup = function (_React$Component) {
    _inherits(BannerGroup, _React$Component);

    function BannerGroup(props) {
        _classCallCheck(this, BannerGroup);

        var _this2 = _possibleConstructorReturn(this, (BannerGroup.__proto__ || Object.getPrototypeOf(BannerGroup)).call(this, props));

        _this2.initHandler = function () {
            var elem = ReactDOM.findDOMNode(_this2);
            var w = elem.offsetWidth;
            _this2.setState({
                show: true,
                width: w,
                height: elem.offsetHeight,
                left: -1 * w * _this2.state.index
            });
            _this2.resetTimer();
        };

        _this2.resetTimer = function () {
            if (!_this2.state.autoPlay) return;
            clearInterval(_this2._auto_timer);
            _this2._auto_timer = setInterval(function () {
                if (!this._onTouching && !this._onAnimate) this.animateTo(this.state.index + 1);
            }.bind(_this2), _this2.state.autoPlay);
        };

        _this2.animateTo = function (targetIndex) {
            var ti = targetIndex;
            var lastIndex = _this2.state.images.length;
            var targetLeft = -_this2.state.width * ti;
            var step = (targetLeft - _this2.state.left) / 15.0;
            _this2._onAnimate = true;

            _this2._timer = setInterval(function () {
                if (Math.abs(this.state.left - targetLeft) <= Math.abs(step * 1.5)) {
                    clearInterval(this._timer);

                    if (ti == 0) {
                        ti = lastIndex;
                        targetLeft = -this.state.width * ti;
                    } else if (ti == lastIndex + 1) {
                        ti = 1;
                        targetLeft = -this.state.width * ti;
                    }
                    if (this.state.index != ti) this.props.afterIndexChange && this.props.afterIndexChange(ti);

                    this._onAnimate = false;
                    this.setState({ left: targetLeft, index: ti });
                } else {
                    this.setState({ left: this.state.left + step });
                }
            }.bind(_this2), 20);
        };

        _this2.touchStartHandler = function (event) {
            if (_this2._onTouching) return;
            _this2._touch.startX = event.changedTouches[0].pageX;
            _this2._touch.originLeft = _this2.state.left;
            _this2._onTouching = true;
            clearInterval(_this2._timer);
        };

        _this2.touchMoveHandler = function (event) {
            var left = _this2._touch.originLeft + event.changedTouches[0].pageX - _this2._touch.startX;
            _this2.setState({ left: left });
            // event.preventDefault();
            event.stopPropagation();
            event.nativeEvent.stopImmediatePropagation();
        };

        _this2.touchEndHandler = function (event) {
            _this2._onTouching = false;

            var delta = event.changedTouches[0].pageX - _this2._touch.startX;

            var ti = _this2.state.index;
            var lastIndex = _this2.state.images.length;

            if (Math.abs(delta) > _this2.state.width / 8) {
                if (delta > 0) {
                    if (_this2.state.index > 1) {
                        ti = _this2.state.index - 1;
                    } else if (_this2.state.loop) {
                        ti = 0;
                    }
                } else {
                    if (_this2.state.index < lastIndex || _this2.state.loop) ti = _this2.state.index + 1;
                }
            }

            _this2.animateTo(ti);
        };

        _this2.imageClickHandler = function (index) {
            _this2.props.onImageClick && _this2.props.onImageClick(index);
        };

        _this2.getDotStyle = function (active) {
            var bg = active ? 'white' : 'hsla(0, 0%, 100%, .25)';
            return {
                display: 'inline-block',
                width: '10px',
                height: '10px',
                background: bg,
                borderRadius: '50%',
                marginRight: '18px'
            };
        };

        _this2._touch = {
            originLeft: 0,
            startX: null,
            startAt: null
        };
        _this2._timer = null;

        _this2._onAnimate = false;
        _this2._onTouching = false;

        var images = _this2.props.images || [];

        var loop = true;
        if (_this2.props.loop === false || images.length <= 1) loop = false;

        var autoPlay = _this2.props.autoPlay || 0;
        autoPlay = images.length <= 1 ? false : autoPlay < 3000 ? 3000 : autoPlay;

        _this2.state = {
            index: _this2.props.startIndex || 1,
            images: images,
            autoPlay: autoPlay,
            loop: loop,
            show: false,
            left: 0,
            width: 0,
            height: 0
        };
        return _this2;
    }

    _createClass(BannerGroup, [{
        key: 'componentDidMount',
        value: function componentDidMount() {
            this.initHandler();
            window.addEventListener('resize', this.initHandler);
        }
    }, {
        key: 'componentWillUnmount',
        value: function componentWillUnmount() {
            clearInterval(this._auto_timer);
            clearInterval(this._timer);
        }
    }, {
        key: 'render',
        value: function render() {
            var _this3 = this;

            var _this = this;

            var style = {
                overflow: 'hidden',
                visibility: this.state.show ? 'visible' : 'hidden',
                position: 'relative',
                transform: 'translate3d(0, 0, 0)',
                background: '#fff'
            };

            var image = function image(img, index) {
                return React.createElement('img', { key: index,
                    onClick: function onClick() {
                        return _this.imageClickHandler(index);
                    },
                    className: index + 1 == _this3.state.index ? 'active' : null,
                    style: {
                        display: 'block',
                        float: 'left',
                        width: _this.state.width + 'px'
                    }, src: img });
            };

            var imitateStyle = { width: this.state.width + 'px', height: '100%', float: 'left' };
            var imitateFirst = React.createElement('div', { style: imitateStyle });
            var imitateLast = React.createElement('div', { style: imitateStyle });
            if (this.state.loop) {
                imitateFirst = image(this.state.images[this.state.images.length - 1], 99);
                imitateLast = image(this.state.images[0], 100);
            }

            var dot = function dot(_, index) {
                var active = index + 1 == _this3.state.index;
                return React.createElement('div', { className: active ? "dot active" : "dot", style: _this3.getDotStyle(active), key: index });
            };

            return React.createElement(
                'div',
                { className: this.props.className + " global-banner-group", style: style },
                React.createElement(
                    'div',
                    { style: {
                            width: this.state.width * (this.state.images.length + 2),
                            position: 'absolute',
                            height: '100%',
                            left: this.state.left
                        },
                        onTouchStart: this.touchStartHandler,
                        onTouchMove: this.touchMoveHandler,
                        onTouchEnd: this.touchEndHandler
                    },
                    imitateFirst,
                    this.state.images.map(image),
                    imitateLast
                ),
                React.createElement(
                    'div',
                    { className: 'dots', style: {
                            position: 'absolute',
                            bottom: '0',
                            height: '30px',
                            width: '100%',
                            textAlign: 'center'
                        } },
                    this.state.images.map(dot)
                )
            );
        }
    }]);

    return BannerGroup;
}(React.Component);

BannerGroup.propTypes = {
    startIndex: PropTypes.number,
    autoPlay: PropTypes.number,
    loop: PropTypes.bool,
    className: PropTypes.string,
    afterIndexChange: PropTypes.func,
    onImageClick: PropTypes.func,
    images: PropTypes.array
};
;;"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var GlobalToast = function (_React$Component) {
    _inherits(GlobalToast, _React$Component);

    function GlobalToast() {
        _classCallCheck(this, GlobalToast);

        var _this = _possibleConstructorReturn(this, (GlobalToast.__proto__ || Object.getPrototypeOf(GlobalToast)).call(this));

        _this.hideHandler = function () {
            _this.setState({ opacity: 0 });
            setTimeout(function () {
                ReactDOM.unmountComponentAtNode(document.getElementById(_this.props.id));
            }, _this.props.animation);
        };

        _this.state = { offset: 0, opacity: 0 };
        return _this;
    }

    _createClass(GlobalToast, [{
        key: "componentDidMount",
        value: function componentDidMount() {
            this.timer = setTimeout(this.hideHandler, this.props.duration);
            this.setState({
                offset: ReactDOM.findDOMNode(this.refs.self).offsetWidth,
                opacity: '1'
            });
        }
    }, {
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
            clearTimeout(this.timer);
            this.props.unMountToast && this.props.unMountToast();
        }
    }, {
        key: "render",
        value: function render() {
            var style = {
                position: "fixed",
                textAlign: "center",
                top: "50%",
                left: "50%",
                transform: "translate(-50%,-50%)",
                WebkitTransform: "translate(-50%,-50%)",
                padding: "18px 28px",
                color: "#fff",
                fontSize: "28px",
                backgroundColor: "rgba(0, 0, 0, 0.6)",
                transition: "opacity " + this.props.animation + "ms ease-in-out",
                opacity: this.state.opacity,
                borderRadius: "5px",
                zIndex: "999"
            };

            return React.createElement(
                "div",
                { className: "error-tip", style: style, ref: "self" },
                this.props.text
            );
        }
    }]);

    return GlobalToast;
}(React.Component);

GlobalToast.defaultProps = {
    duration: 2000,
    animation: 200
};;"use strict";

var Grid_1 = React.createClass({
    displayName: "Grid_1",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
            // page: 1,
            // page_count: "",
            // hasData: true
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
        // $FW.Event.touchBottom(this.loadMoreProductHandler);
    },

    // loadMoreProductHandler: function (done) {
    //     this.setState({ page: this.state.page + 1 });
    //     // if (!this.state.hasData) {
    //     //     $FW.Event.cancelTouchBottom();
    //     //     return;
    //     // }

    //     $FW.Ajax({
    //         url: `${API_PATH}/mall/api/index/v1/recommendProducts.json`,
    //         data: {
    //             count: this.state.page_count,
    //             page: this.state.page
    //         }
    //     }).then(data => {
    //         let products = data.products;
    //         this.setState({
    //             products: [...this.state.products, ...products],
    //             hasData: !!products.length
    //         })
    //         done && done()
    //     })
    // },
    render: function render() {
        var products = this.state.products;

        if (products.length === 0) return null;

        var theme2_top_product_item = function theme2_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme2-top-product-item", key: index,
                    href: productLink(product.bizNo) },
                React.createElement(
                    "div",
                    { className: "theme2-top-product-title theme2-top-product-title-color" + parseInt(index + 1) },
                    product.abbreviation
                ),
                React.createElement(
                    "div",
                    { className: "theme2-top-product-price" },
                    product.rmbPrice != 0 && "\xA5" + product.rmbPrice,
                    product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                    product.score != 0 && product.score + "\u5DE5\u5206"
                ),
                React.createElement("img", { className: "product-img2", src: product.img })
            );
        };

        var theme2_btm_product_item = function theme2_btm_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme2-btm-product-item", key: index,
                    href: productLink(product.bizNo) },
                React.createElement(
                    "div",
                    { className: "theme2-btm-product-wrap" },
                    React.createElement("img", { className: "theme2-product-img", src: product.img }),
                    React.createElement(
                        "div",
                        { className: "theme2-btm-product-info" },
                        React.createElement(
                            "span",
                            { className: "theme2-btm-product-title" },
                            product.abbreviation
                        ),
                        React.createElement(
                            "span",
                            { className: "theme2-btm-product-price" },
                            product.rmbPrice != 0 && "\xA5" + product.rmbPrice,
                            product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                            product.score,
                            "\u5DE5\u5206"
                        )
                    )
                )
            );
        };

        return React.createElement(
            "div",
            { className: "theme-2" },
            React.createElement(
                "a",
                { className: "activity-theme", href: "/static/mall/product-list/index.html?searchSourceType=0&category=workshop&title=\u5DE5\u573A\u5238" },
                React.createElement("img", { src: "images/education-theme-img.png" })
            ),
            React.createElement(
                "div",
                { className: "theme2-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme2-top-product-list" },
                    products.slice(0, 3).map(theme2_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme2-btm-product-list" },
                    products.slice(3, 9).map(theme2_btm_product_item)
                )
            )
        );
    }
});;"use strict";

var Grid_2 = React.createClass({
    displayName: "Grid_2",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
            // page: 1,
            // page_count: "",
            // hasData: true
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
        // $FW.Event.touchBottom(this.loadMoreProductHandler);
    },

    // loadMoreProductHandler: function (done) {
    //     this.setState({ page: this.state.page + 1 });
    //     // if (!this.state.hasData) {
    //     //     $FW.Event.cancelTouchBottom();
    //     //     return;
    //     // }

    //     $FW.Ajax({
    //         url: `${API_PATH}/mall/api/index/v1/recommendProducts.json`,
    //         data: {
    //             count: this.state.page_count,
    //             page: this.state.page
    //         }
    //     }).then(data => {
    //         let products = data.products;
    //         this.setState({
    //             products: [...this.state.products, ...products],
    //             hasData: !!products.length
    //         })
    //         done && done()
    //     })
    // },
    render: function render() {
        var products = this.state.products;

        if (!products || products.length === 0) return null;

        var theme_product_item = function theme_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme-product-item", key: index,
                    href: productLink(product.bizNo) },
                React.createElement("img", { className: "theme-1-img", src: product.img }),
                React.createElement(
                    "span",
                    { className: "theme-product-item-name" },
                    product.abbreviation
                )
            );
        };
        return React.createElement(
            "div",
            { className: "theme-1" },
            React.createElement(
                "a",
                { className: "activity-theme",
                    href: "/static/mall/product-list/index.html?searchSourceType=0&category=diet&title=\u996E\u98DF" },
                React.createElement("img", { src: "/static/mall/home/images/diet.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme-product-wrap" },
                products.map(theme_product_item)
            )
        );
    }
});;"use strict";

function fmtPrice(v) {
    return v != 0 && "\xA5" + v + "+";
}

var Grid_3 = React.createClass({
    displayName: "Grid_3",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme4_top_product_item = function theme4_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme4-top-product-item", key: index,
                    href: productLink(product.bizNo) },
                React.createElement(
                    "span",
                    {
                        className: "theme4-top-product-title theme4-top-product-title-color" + parseInt(index + 1) },
                    product.abbreviation
                ),
                React.createElement(
                    "span",
                    {
                        className: "theme4-top-product-price" },
                    product.rmbPrice == 0 ? null : "\xA5" + product.rmbPrice,
                    product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                    product.score == 0 ? null : product.score + "\u5DE5\u5206"
                ),
                React.createElement("span", { className: "horizon-line theme4-top-line-color" + parseInt(index + 1) }),
                React.createElement("img", { className: "theme4-top-product-img", src: product.img })
            );
        };

        var get_prd = function get_prd(n) {
            return products[n] || {};
        };

        return React.createElement(
            "div",
            { className: "theme-4" },
            React.createElement(
                "a",
                { className: "activity-theme", href: "/static/mall/product-list/index.html?searchSourceType=0&category=living&title=\u5BB6\u5C45\u751F\u6D3B" },
                React.createElement("img", { src: "/static/mall/home/images/living.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme4-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme4-top-product-list" },
                    products.slice(0, 4).map(theme4_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme4-btm-product-list" },
                    React.createElement(
                        "div",
                        { className: "theme4-btm-left-product-item" },
                        React.createElement(
                            "a",
                            { className: "theme4-btm-left-product-wrap", href: productLink(products[4].bizNo) },
                            React.createElement("img", { className: "theme4-btm-product-img", src: products[4].img }),
                            React.createElement(
                                "span",
                                {
                                    className: "theme4-btm-product-title theme4-btm-product-title-color1" },
                                products[4].abbreviation
                            ),
                            React.createElement(
                                "span",
                                {
                                    className: "theme4-btm-product-price" },
                                fmtPrice(products[4].rmbPrice),
                                products[4].score,
                                "\u5DE5\u5206"
                            ),
                            React.createElement(
                                "span",
                                { className: "product-purchase theme4-btm-product-title-color1" },
                                "\u70B9\u51FB\u62A2\u8D2D",
                                React.createElement("span", {
                                    className: "tri tri-btm-color1" })
                            )
                        )
                    ),
                    React.createElement(
                        "div",
                        { className: "theme4-btm-middle-product-wrap" },
                        React.createElement(
                            "a",
                            { className: "theme4-btm-middle-product-item",
                                href: "/static/mall/product-detail/index.html?bizNo=" + products[5].bizNo },
                            React.createElement(
                                "div",
                                { className: "theme4-btm-middle-top-product-wrap" },
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-img-wrap" },
                                    React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(5).img })
                                ),
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-middle-top-info" },
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-title theme4-btm-product-title-color2" },
                                        get_prd(5).abbreviation
                                    ),
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-price" },
                                        fmtPrice(get_prd(5).rmbPrice),
                                        get_prd(5).score,
                                        "\u5DE5\u5206"
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "product-purchase theme4-btm-product-title-color2" },
                                        "\u70B9\u51FB\u62A2\u8D2D",
                                        React.createElement("span", {
                                            className: "tri tri-btm-color2" })
                                    )
                                )
                            )
                        ),
                        React.createElement(
                            "a",
                            { className: "theme4-btm-middle-product-item",
                                href: "/static/mall/product-detail/index.html?bizNo=" + get_prd(6).bizNo },
                            React.createElement(
                                "div",
                                { className: "theme4-btm-middle-top-product-wrap" },
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-img-wrap" },
                                    React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(6).img })
                                ),
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-middle-top-info" },
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-title theme4-btm-product-title-color3" },
                                        this.state.productSeventhTitle
                                    ),
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-price" },
                                        fmtPrice(get_prd(6).rmbPrice),
                                        get_prd(6).score,
                                        "\u5DE5\u5206"
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "product-purchase theme4-btm-product-title-color3" },
                                        "\u70B9\u51FB\u62A2\u8D2D",
                                        React.createElement("span", {
                                            className: "tri tri-btm-color3" })
                                    )
                                )
                            )
                        )
                    ),
                    React.createElement(
                        "div",
                        { className: "theme4-btm-right-product-wrap" },
                        React.createElement(
                            "a",
                            { href: '/static/mall/product-detail/index.html?bizNo=' + get_prd(7).bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(7).img })
                        ),
                        React.createElement(
                            "a",
                            { href: '/static/mall/product-detail/index.html?bizNo=' + get_prd(8).bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(8).img })
                        )
                    )
                )
            )
        );
    }
});;"use strict";

var Grid_4 = React.createClass({
    displayName: "Grid_4",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme3_top_product_item = function theme3_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme3-top-product-item", key: index,
                    href: '/static/mall/product-detail/index.html?bizNo=' + product.bizNo },
                React.createElement(
                    "div",
                    { className: "theme3-top-product-wrap" },
                    React.createElement(
                        "div",
                        { className: "theme3-top-product-info" },
                        React.createElement(
                            "span",
                            {
                                className: "theme3-top-product-title theme3-top-product-title-color" + parseInt(index + 1) },
                            product.abbreviation
                        ),
                        React.createElement(
                            "span",
                            {
                                className: "theme3-top-product-price" },
                            product.rmbPrice != 0 && "¥" + product.rmbPrice,
                            product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                            product.score != 0 && product.score + "工分"
                        ),
                        React.createElement(
                            "span",
                            {
                                className: "product-purchase theme3-top-product-title-color" + parseInt(index + 1) },
                            "\u70B9\u51FB\u62A2\u8D2D",
                            React.createElement("span", {
                                className: "tri tri-color" + parseInt(index + 1) })
                        )
                    ),
                    React.createElement("img", { className: "theme3-product-img", src: product.img })
                )
            );
        };

        var theme3_btm_product_item = function theme3_btm_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme3-btm-product-item", key: index,
                    href: '/static/mall/product-detail/index.html?bizNo=' + product.bizNo },
                React.createElement("img", { className: "theme3-btm-product-img", src: product.img }),
                React.createElement(
                    "span",
                    { className: "theme3-btm-product-title" },
                    product.abbreviation
                )
            );
        };

        return React.createElement(
            "div",
            { className: "theme-3" },
            React.createElement(
                "a",
                { href: "/static/mall/product-list/index.html?searchSourceType=0&category=automobile&title=\u6C7D\u8F66\u7528\u54C1", className: "activity-theme" },
                React.createElement("img", { src: "/static/mall/home/images/automobile.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme3-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme3-top-product-list" },
                    products.slice(0, 6).map(theme3_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme3-btm-product-list" },
                    products.slice(6, 10).map(theme3_btm_product_item)
                )
            )
        );
    }
});;"use strict";

var Grid_5 = React.createClass({
    displayName: "Grid_5",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme2_top_product_item = function theme2_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme2-top-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement(
                    "div",
                    {
                        className: "theme2-top-product-title theme2-top-product-title-color" + parseInt(index + 1) },
                    product.abbreviation
                ),
                React.createElement(
                    "div",
                    { className: "theme2-top-product-price" },
                    product.rmbPrice == 0 ? null : "\xA5" + product.rmbPrice,
                    product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                    product.score == 0 ? null : product.score + "\u5DE5\u5206"
                ),
                React.createElement("img", { className: "product-img2", src: product.img })
            );
        };

        var theme2_btm_product_item = function theme2_btm_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme2-btm-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement(
                    "div",
                    { className: "theme2-btm-product-wrap" },
                    React.createElement("img", { className: "theme2-product-img", src: product.img }),
                    React.createElement(
                        "div",
                        { className: "theme2-btm-product-info" },
                        React.createElement(
                            "span",
                            { className: "theme2-btm-product-title" },
                            product.abbreviation
                        ),
                        React.createElement(
                            "span",
                            { className: "theme2-btm-product-price" },
                            product.rmbPrice != 0 && "\xA5" + product.rmbPrice,
                            product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                            product.score != 0 && product.score + "\u5DE5\u5206"
                        )
                    )
                )
            );
        };
        return React.createElement(
            "div",
            { className: "theme-2" },
            React.createElement(
                "a",
                { href: "/static/mall/product-list/index.html?searchSourceType=0&category=outdoor&title=\u6237\u5916\u7528\u54C1",
                    className: "activity-theme" },
                React.createElement("img", { src: "/static/mall/home/images/outdoor.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme2-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme2-top-product-list" },
                    products.slice(0, 3).map(theme2_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme2-btm-product-list" },
                    products.slice(3, 9).map(theme2_btm_product_item)
                )
            )
        );
    }
});;"use strict";

var Grid_6 = React.createClass({
    displayName: "Grid_6",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme_product_item = function theme_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement("img", { className: "theme-1-img", src: product.img }),
                React.createElement(
                    "span",
                    { className: "theme-product-item-name" },
                    product.abbreviation
                )
            );
        };
        return React.createElement(
            "div",
            { className: "theme-1" },
            React.createElement(
                "a",
                { className: "activity-theme", href: "/static/mall/product-list/index.html?searchSourceType=0&category=mobileDigital&title=\u624B\u673A\u6570\u7801" },
                React.createElement("img", { src: "/static/mall/home/images/mobileDigital.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme-product-wrap" },
                products.map(theme_product_item)
            )
        );
    }
});;"use strict";

var Grid_7 = React.createClass({
    displayName: "Grid_7",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme4_top_product_item = function theme4_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme4-top-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement(
                    "span",
                    {
                        className: "theme4-top-product-title theme4-top-product-title-color" + parseInt(index + 1) },
                    product.abbreviation
                ),
                React.createElement(
                    "span",
                    { className: "theme4-top-product-price" },
                    product.rmbPrice == 0 ? null : "\xA5" + product.rmbPrice,
                    product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                    product.score == 0 ? null : product.score + "\u5DE5\u5206"
                ),
                React.createElement("span", { className: "horizon-line theme4-top-line-color" + parseInt(index + 1) }),
                React.createElement("img", { className: "theme4-top-product-img", src: product.img })
            );
        };

        var get_prd = function get_prd(n) {
            return products[n] || {};
        };

        return React.createElement(
            "div",
            { className: "theme-4" },
            React.createElement(
                "a",
                { href: "/static/mall/product-list/index.html?searchSourceType=0&category=maternalInfantEducation&title=\u6BCD\u5A74\u6559\u80B2", className: "activity-theme" },
                React.createElement("img", { src: "/static/mall/home/images/maternalInfantEducation.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme4-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme4-top-product-list" },
                    products.slice(0, 4).map(theme4_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme4-btm-product-list" },
                    React.createElement(
                        "div",
                        { className: "theme4-btm-left-product-item" },
                        React.createElement(
                            "a",
                            { className: "theme4-btm-left-product-wrap",
                                href: "/static/mall/product-detail/index.html?bizNo=" + products[4].bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: products[4].img }),
                            React.createElement(
                                "span",
                                {
                                    className: "theme4-btm-product-title theme4-btm-product-title-color1" },
                                products[4].abbreviation
                            ),
                            React.createElement(
                                "span",
                                {
                                    className: "theme4-btm-product-price" },
                                products[4].rmbPrice == 0.00 ? null : "¥" + products[4].rmbPrice + "+",
                                products[4].score,
                                "\u5DE5\u5206"
                            ),
                            React.createElement(
                                "span",
                                { className: "product-purchase theme4-btm-product-title-color1" },
                                "\u70B9\u51FB\u62A2\u8D2D",
                                React.createElement("span", {
                                    className: "tri tri-btm-color1" })
                            )
                        )
                    ),
                    React.createElement(
                        "div",
                        { className: "theme4-btm-middle-product-wrap" },
                        React.createElement(
                            "a",
                            { className: "theme4-btm-middle-product-item",
                                href: '/static/mall/product-detail/index.html?bizNo=' + products[5].bizNo },
                            React.createElement(
                                "div",
                                { className: "theme4-btm-middle-top-product-wrap" },
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-img-wrap" },
                                    React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(5).img })
                                ),
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-middle-top-info" },
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-title theme4-btm-product-title-color2" },
                                        get_prd(5).abbreviation
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "theme4-btm-product-price" },
                                        get_prd(5).rmbPrice != 0 && "\xA5" + get_prd(5).rmbPrice + "+",
                                        get_prd(5).score,
                                        "\u5DE5\u5206"
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "product-purchase theme4-btm-product-title-color2" },
                                        "\u70B9\u51FB\u62A2\u8D2D",
                                        React.createElement("span", { className: "tri tri-btm-color2" })
                                    )
                                )
                            )
                        ),
                        products[6] ? React.createElement(
                            "a",
                            { className: "theme4-btm-middle-product-item",
                                href: "/static/mall/product-detail/index.html?bizNo=" + get_prd(6).bizNo },
                            React.createElement(
                                "div",
                                { className: "theme4-btm-middle-top-product-wrap" },
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-img-wrap" },
                                    React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(6).img })
                                ),
                                React.createElement(
                                    "div",
                                    { className: "theme4-btm-middle-top-info" },
                                    React.createElement(
                                        "span",
                                        {
                                            className: "theme4-btm-product-title theme4-btm-product-title-color3" },
                                        this.state.productSeventhTitle
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "theme4-btm-product-price" },
                                        get_prd(6).rmbPrice != 0 && "\xA5" + get_prd(6).rmbPrice + "+",
                                        get_prd(6).score,
                                        "\u5DE5\u5206"
                                    ),
                                    React.createElement(
                                        "span",
                                        { className: "product-purchasej theme4-btm-product-title-color3" },
                                        "\u70B9\u51FB\u62A2\u8D2D",
                                        React.createElement("span", { className: "tri tri-btm-color3" })
                                    )
                                )
                            )
                        ) : null
                    ),
                    products[7] ? React.createElement(
                        "div",
                        { className: "theme4-btm-right-product-wrap" },
                        React.createElement(
                            "a",
                            { href: "/static/mall/product-detail/index.html?bizNo=" + get_prd(7).bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(7).img })
                        )
                    ) : null,
                    products[8] ? React.createElement(
                        "div",
                        { className: "theme4-btm-right-product-wrap" },
                        React.createElement(
                            "a",
                            { href: "/static/mall/product-detail/index.html?bizNo=" + get_prd(7).bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(7).img })
                        ),
                        React.createElement(
                            "a",
                            { href: "/static/mall/product-detail/index.html?bizNo=" + get_prd(8).bizNo },
                            React.createElement("img", { className: "theme4-btm-product-img", src: get_prd(8).img })
                        )
                    ) : null
                )
            )
        );
    }
});;"use strict";

var Grid_8 = React.createClass({
    displayName: "Grid_8",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },
    render: function render() {
        var products = this.state.products;

        if (products.length == 0) return null;

        var theme3_top_product_item = function theme3_top_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme3-top-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement(
                    "div",
                    { className: "theme3-top-product-wrap" },
                    React.createElement(
                        "div",
                        { className: "theme3-top-product-info" },
                        React.createElement(
                            "span",
                            {
                                className: "theme3-top-product-title theme3-top-product-title-color" + parseInt(index + 1) },
                            product.abbreviation
                        ),
                        React.createElement(
                            "span",
                            {
                                className: "theme3-top-product-price" },
                            product.rmbPrice == 0 ? null : "¥" + product.rmbPrice,
                            product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                            product.score == 0 ? null : product.score + "工分"
                        ),
                        React.createElement(
                            "span",
                            {
                                className: "product-purchase theme3-top-product-title-color" + parseInt(index + 1) },
                            "\u70B9\u51FB\u62A2\u8D2D",
                            React.createElement("span", {
                                className: "tri tri-color" + parseInt(index + 1) })
                        )
                    ),
                    React.createElement("img", { className: "theme3-product-img", src: product.img })
                )
            );
        };

        var theme3_btm_product_item = function theme3_btm_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "theme3-btm-product-item", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement("img", { className: "theme3-btm-product-img", src: product.img }),
                React.createElement(
                    "span",
                    { className: "theme3-btm-product-title" },
                    product.abbreviation
                )
            );
        };

        return React.createElement(
            "div",
            { className: "theme-3" },
            React.createElement(
                "a",
                { href: "/static/mall/product-list/index.html?searchSourceType=0&category=qualityLife&title=\u54C1\u8D28\u751F\u6D3B", className: "activity-theme" },
                React.createElement("img", {
                    src: "/static/mall/home/images/qualityLife.jpg" })
            ),
            React.createElement(
                "div",
                { className: "theme3-product-wrap" },
                React.createElement(
                    "div",
                    { className: "theme3-top-product-list" },
                    products.slice(0, 6).map(theme3_top_product_item)
                ),
                React.createElement(
                    "div",
                    { className: "theme3-btm-product-list" },
                    products.slice(6, 10).map(theme3_btm_product_item)
                )
            )
        );
    }
});;"use strict";

var HotProducts = React.createClass({
    displayName: "HotProducts",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            ps: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ ps: data.products || [] });
        });
    },

    render: function render() {
        if (this.state.ps.length === 0) return null;

        var hot_product_item = function hot_product_item(product, index) {
            return React.createElement(
                "a",
                { className: "hot-product-item hot-product-item-bg-" + (index + 1), key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement("img", { className: "hot-img", src: product.img }),
                React.createElement(
                    "span",
                    { className: "hot-img-title hot-img-title-color-" + parseInt(index + 1) },
                    product.abbreviation
                ),
                React.createElement(
                    "span",
                    {
                        className: "hot-img-price" },
                    product.rmbPrice != 0 && "\xA5" + product.rmbPrice,
                    product.rmbPrice == 0 || product.score == 0 ? "" : "+",
                    product.score != 0 && product.score + "\u5DE5\u5206"
                )
            );
        };
        return React.createElement(
            "div",
            { className: "hot-product-list" },
            React.createElement(
                "div",
                { className: "hot-title" },
                React.createElement(
                    "b",
                    null,
                    "\u2014\u2014 "
                ),
                "\u5954\u5411\u6237\u5916 \u7ED9\u538B\u529B\u6253\u6298",
                React.createElement(
                    "b",
                    null,
                    " \u2014\u2014"
                ),
                React.createElement(
                    "a",
                    { className: "link-more", href: "/static/mall/activity/index.html?&bizNo=D0000001201" },
                    "\u66F4\u591A > "
                )
            ),
            React.createElement(
                "div",
                { className: "hot-product-wrap" },
                this.state.ps.map(hot_product_item)
            )
        );
    }
});;"use strict";

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

var HotSales = React.createClass({
    displayName: "HotSales",

    getInitialState: function getInitialState() {
        return {
            page: 1,
            page_count: 6,
            hasData: true,
            products: []
        };
    },

    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/hotProducts.json?count=" + this.state.page_count,
            enable_loading: 'mini'
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
        $FW.Event.touchBottom(this.loadMoreProductHandler);
    },

    loadMoreProductHandler: function loadMoreProductHandler(done) {
        var _this2 = this;

        this.setState({ page: this.state.page + 1 });
        if (!this.state.hasData) {
            $FW.Event.cancelTouchBottom();
            return;
        }

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/hotProducts.json", //人气热卖列表
            data: {
                count: this.state.page_count,
                page: this.state.page
            }
        }).then(function (data) {
            var products = data.products;
            _this2.setState({
                products: [].concat(_toConsumableArray(_this2.state.products), _toConsumableArray(products)),
                hasData: !!products.length
            });
            done && done();
        });
    },

    render: function render() {
        var hotProduct = function hotProduct(product, index) {
            return React.createElement(
                "a",
                { className: "product-wrap", key: index,
                    href: "/static/mall/product-detail/index.html?bizNo=" + product.bizNo },
                React.createElement("img", { src: product.img }),
                React.createElement(
                    "span",
                    { className: "product-name" },
                    product.title
                ),
                React.createElement(
                    "span",
                    { className: "product-price" },
                    product.price != 0 && "\xA5" + product.price,
                    product.price == 0 || product.score == 0 ? "" : "+",
                    product.score != 0 && product.score + "\u5DE5\u5206"
                )
            );
        };

        return React.createElement(
            "div",
            { className: "hot-sales" },
            React.createElement(
                "div",
                { className: "hot-sales-title" },
                React.createElement("img", { src: "images/hot-sale.png" })
            ),
            React.createElement(
                "div",
                { className: "product-list" },
                this.state.products.map(hotProduct)
            )
        );
    }
});;"use strict";

var NewProducts = React.createClass({
    displayName: "NewProducts",
    getInitialState: function getInitialState() {
        return {
            bizNo: this.props.bizNo,
            products: []
        };
    },
    componentDidMount: function componentDidMount() {
        var _this = this;

        $FW.Ajax({
            url: API_PATH + "/mall/api/index/v1/recommendProducts.json",
            data: {
                recommendBizNo: this.state.bizNo,
                totalCount: this.props.count
            }
        }).then(function (data) {
            return _this.setState({ products: data.products });
        });
    },

    render: function render() {
        var products = this.state.products;

        if (!products || products.length == 0) return null;

        var product1 = products[0] && React.createElement(
            "div",
            { className: "new-left-product" },
            React.createElement(
                "a",
                { className: "new-bg-1",
                    href: "/static/mall/product-detail/index.html?bizNo=" + products[0].bizNo },
                React.createElement("img", { className: "new-img1", src: products[0].img }),
                React.createElement(
                    "span",
                    { className: "new-img1-title" },
                    products[0].abbreviation
                )
            )
        );

        var product2 = products[1] && React.createElement(
            "a",
            { className: "new-bg-2",
                href: "/static/mall/product-detail/index.html?bizNo=" + products[1].bizNo },
            React.createElement(
                "div",
                { className: "new-right-top-wrap" },
                React.createElement("img", { className: "new-img2", src: products[1].img }),
                React.createElement(
                    "div",
                    { className: "new-right-top-product-info" },
                    React.createElement(
                        "span",
                        { className: "new-img2-title" },
                        products[1].abbreviation
                    )
                )
            )
        );

        var product3 = products[2] && React.createElement(
            "a",
            { className: "new-bg-3",
                href: "/static/mall/product-detail/index.html?bizNo=" + products[2].bizNo },
            React.createElement(
                "div",
                { className: "new-right-top-wrap" },
                React.createElement("img", { className: "new-img2", src: products[2].img }),
                React.createElement(
                    "div",
                    { className: "new-right-top-product-info" },
                    React.createElement(
                        "span",
                        { className: "new-img2-title" },
                        products[2].abbreviation
                    )
                )
            )
        );

        var product4 = products[3] && React.createElement(
            "a",
            { className: "new-bg-4",
                href: "/static/mall/product-detail/index.html?bizNo=" + products[3].bizNo },
            React.createElement("img", { className: "new-img2", src: products[3].img }),
            React.createElement(
                "span",
                { className: "new-img2-title" },
                products[3].abbreviation
            )
        );

        var product5 = products[4] && React.createElement(
            "a",
            { className: "new-bg-5",
                href: "/static/mall/product-detail/index.html?bizNo=" + products[4].bizNo },
            React.createElement("img", { className: "new-img2", src: products[4].img }),
            React.createElement(
                "span",
                { className: "new-img2-title" },
                products[4].abbreviation
            )
        );

        var product6 = products[5] && React.createElement(
            "a",
            { className: "new-bg-6",
                href: "/static/mall/product-detail/index.html?bizNo=" + products[5].bizNo },
            React.createElement("img", { className: "new-img2", src: products[5].img }),
            React.createElement(
                "span",
                { className: "new-img2-title" },
                products[5].abbreviation
            )
        );

        return React.createElement(
            "div",
            { className: "new-product-list" },
            React.createElement(
                "div",
                { className: "new-title" },
                React.createElement("img", { className: "new-title-img", src: "images/new-title.png" })
            ),
            React.createElement(
                "div",
                { className: "new-product-wrap" },
                product1,
                React.createElement(
                    "div",
                    { className: "new-right-product" },
                    React.createElement(
                        "div",
                        { className: "new-right-top-product" },
                        product2,
                        product3
                    ),
                    React.createElement(
                        "div",
                        { className: "new-right-btm-product" },
                        product4,
                        product5,
                        product6
                    )
                )
            )
        );
    }
});;"use strict";

var Mall = React.createClass({
    displayName: "Mall",

    getInitialState: function getInitialState() {
        return {
            timer: null
        };
    },

    verifyForm: function verifyForm() {
        var username = document.querySelector("#username").value;
        var password = document.querySelector("#password").value;
        if (username == null || username == "" || password == null || password == "") {
            document.querySelector("#nextBtn").classList.add("disabled");
        } else {
            document.querySelector("#nextBtn").classList.remove("disabled");
        }
    },

    btnClickLogin: function btnClickLogin() {
        if (document.querySelector("#nextBtn").classList.contains('disabled')) return;
        $FW.Ajax({
            url: API_PATH + "login",
            data: {
                mapping_id: document.querySelector('#username').value,
                username: document.querySelector('#username').value,
                password: document.querySelector('#password').value
            },
            method: 'post'
        }).then(function (data) {});
        this.setTimeout(function () {
            alert(2);
        }, 500);
    },

    interval_login: function interval_login() {
        alert(1);
        var interval_login = setInterval(function () {
            // 	$FW.Ajax({
            // 		url: `${API_PATH}/tasks/status`
            // 	}).then(data => {
            // 		document.querySelector('#message').innerHTML=data.description; //反馈信息
            //        if ((data.phase == "LOGIN" && data.phase_status == "SUCCESS")) {//认证成功！
            //            clearInterval(interval_login);
            //            this.crawler(); //调用数据采集接口
            //
            //        }
            //        if ((data.phase == "LOGIN" && data.phase_status == "ERROR")) {//登录失败！
            //            clearInterval(interval_login);
            //
            //            return;
            //        }
            // 	})
        }, 1000);
    },
    crawler: function crawler() {},
    componentDidMount: function componentDidMount() {
        var Li = document.querySelectorAll(".inputstyle");
        for (var i = 0; i < Li.length; i++) {
            Li[i].addEventListener('input', function () {
                this.verifyForm();
            }.bind(this));
        }
    },
    render: function render() {
        return React.createElement(
            "div",
            { className: "content" },
            React.createElement(
                "div",
                { id: "error_tips" },
                React.createElement(
                    "div",
                    { id: "error_tips_content" },
                    React.createElement("span", { id: "error_icon" }),
                    React.createElement("span", { id: "error_message" })
                )
            ),
            React.createElement(
                "div",
                { id: "login", className: "login" },
                React.createElement("div", { id: "logo", className: "logo" }),
                React.createElement(
                    "div",
                    { id: "web_login" },
                    React.createElement(
                        "ul",
                        { id: "g_list" },
                        React.createElement(
                            "li",
                            { id: "g_u" },
                            React.createElement(
                                "div",
                                { id: "del_touch", className: "del_touch" },
                                React.createElement("span", { id: "del_u", className: "del_u" })
                            ),
                            React.createElement("input", { id: "username", className: "inputstyle", type: "text", name: "u", autoComplete: "off", placeholder: "QQ\u53F7\u7801/\u624B\u673A/\u90AE\u7BB1" })
                        ),
                        React.createElement(
                            "li",
                            { id: "g_p" },
                            React.createElement(
                                "div",
                                { id: "del_touch_p", className: "del_touch" },
                                React.createElement("span", { id: "del_p", className: "del_u" })
                            ),
                            React.createElement("input", { id: "password", className: "inputstyle", maxLength: "16", type: "password", name: "p", autoCorrect: "off", placeholder: "\u8BF7\u8F93\u5165\u4F60\u7684QQ\u5BC6\u7801" })
                        )
                    ),
                    React.createElement(
                        "div",
                        { href: "javascript:void(0);", className: "disabled", onClick: this.btnClickLogin, id: "nextBtn" },
                        "\u767B \u5F55"
                    )
                )
            )
        );
    }
});

$FW.DOMReady(function () {
    ReactDOM.render(React.createElement(Mall, null), CONTENT_NODE);
});