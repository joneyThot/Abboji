/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var local_user_pro, all_locations, serviceUrl, imageUrl, adverts_data, category_data, premiumoffers_data, todayspecial_data, search_result_data, select_category_list, current_lat, current_lun, total_list_cat_count, total_today_cat_count, card_detail_data;
var page_load_count = 0;
var shopping_list = {},
    local_list = localStorage.getItem("shopping_list");
if (local_list) {
    shopping_list = JSON.parse(local_list);
}

local_user_pro = localStorage.getItem("user_profile");
serviceUrl = "http://108.163.162.132/abooji/abooji_webservice/";
imageUrl = "http://108.163.162.132/abooji/uploads/discount/";

var adverts_load = false,
    category_load = false,
    premiumoffers_load = false,
    card_load = false,
    todayspecial_load = false,
    card_detail_load = false,
    all_data_load = false;

var app = {
    // Application Constructor
    initialize: function () {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        document.addEventListener('offline', this.isOffline, false);
        document.addEventListener('online', this.isOnline, false);
        document.addEventListener('menubutton', this.onMenuKeyDown, false);

    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function () {
        app.receivedEvent('deviceready');
        FastClick.attach(document.body);

        StatusBar.overlaysWebView(false);
        StatusBar.backgroundColorByHexString('#ffffff');
        StatusBar.styleDefault();

        navigator.splashscreen.hide();

        //document.addEventListener("menubutton", onMenuKeyDown, false);
        //document.addEventListener("backbutton", backKeyDown, false);
        //document.addEventListener("searchbutton", searchKeyDown, false);
        //injectMenu();
    },
    // Update DOM on a Received Event
    receivedEvent: function (id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        //console.log('Received Event: ' + id);
    },
    onMenuKeyDown: function () {
        //alert("test menu key");
        //show_popup();
        //scanCode();
        /*$.mobile.changePage("#menu");*/
    },
    isOffline: function () {
        alert("device is OFFline");
        /*navigator.notification.confirm(
            'You are offline plese connect to internet',
            onConfirmQuit,
            'Offline',
            'OK'
        );

        function onConfirmQuit(button) {
            navigator.app.exitApp();
        }*/

    },
    isOnline: function () {
        //alert("device is ONline");
    }
};

/*For create menu*/


$(function () {

    $('#menu').mmenu({
        onClick: {
            blockUI: false,
            preventDefault: function () {
                return this.rel != 'external';
            }
        }
    }, {
        pageSelector: 'div[data-role="page"]:first'
    });

    $('#menu a[rel!="external"]').on(
        'click',
        function () {
            var _t = this;
            $('#menu').one(
                'closed.mm',
                function () {
                    $.mobile.changePage(_t.href);
                }
            );
        }
    );
});

$(document).on(
    'pageshow',
    function (e, ui) {
        $('#menu').trigger('setPage', [$(e.target)]);
        $('#menu a').each(
            function () {
                if ($.mobile.path.parseUrl(this.href).href == window.location.href) {
                    $(this).trigger('setSelected.mm');
                }
            }
        );
    }
);


/*End menu*/




(function (doc, win) {
    var docEl = doc.documentElement,
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;

            docEl.style.fontSize = clientWidth + 'px';
            docEl.style.display = "none";
            docEl.clientWidth; // Force relayout - important to new Androids
            docEl.style.display = "";
        };

    // Abort if browser does not support addEventListener
    if (!doc.addEventListener) return;

    // Test rem support
    var div = doc.createElement('div');
    div.setAttribute('style', 'font-size: 1rem');

    // Abort if browser does not recognize rem
    if (div.style.fontSize != "1rem") return;

    win.addEventListener('resize', recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

$(document).ready(function () {


    var saveonSuccess = function (position) {
        /*        alert('Latitude: ' + position.coords.latitude + '\n' +
            'Longitude: ' + position.coords.longitude + '\n' +
            'Altitude: ' + position.coords.altitude + '\n' +
            'Accuracy: ' + position.coords.accuracy + '\n' +
            'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
            'Heading: ' + position.coords.heading + '\n' +
            'Speed: ' + position.coords.speed + '\n' +
            'Timestamp: ' + position.timestamp + '\n');*/
        current_lat = position.coords.latitude;
        current_lun = position.coords.longitude;

        var current_location = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        localStorage.setItem('current_pos', current_location);
        /* localStorage.setItem('latitude', position.coords.latitude);
        localStorage.setItem('longitude', position.coords.longitude);*/


    };

    // onError Callback receives a PositionError object
    //
    function onError(error) {
        console.error('code: ' + error.code + '\n' +
            'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(saveonSuccess, onError);

    $("#select-category").change(function () {
        filter_list_cat($('#select-category').val());
        //        console.log("in bind change select");
    });


    $("#select-card").change(function () {
        //        filter_card($('#select-card').val());
    });


    /*For range slider on today's spl page*/
    $("#slider").slider();
    $('#slider').slider().bind({
        slidestart: function (event, ui) {},
        slide: function (event, ui) {
            //console.warn("slider value: " + $('#slider').val());
        },
        slidestop: function (event, ui) {
            //console.info("slider value: " + $('#slider').val());
            //            filter_range($('#slider').val());
        }
    });

    /*For range slider on listing page*/
    $("#list_select_range").slider();

    /*For range slider on search result page*/
    $("#search_pg_slider").slider();

    $("#loc_slider").change(function () {
        //console.info("loc_change: "+$("#loc_slider").val());

        if ($("#loc_slider").val() == "gps") {
            $("#btn_loc_gps").removeClass('ui-disabled');
            $("#btn_addloc_map").addClass('ui-disabled');
        } else {
            $("#btn_addloc_map").removeClass('ui-disabled');
            $("#btn_loc_gps").addClass('ui-disabled');
        }

    });


    if (local_user_pro == null || local_user_pro == undefined) {
        $.mobile.changePage("#startupinfo");
        $('#loc_name').val("Home");
    }

});



/*
$(document).on( "pagechange", function( event ) {
    console.log("pagechange event");
    }
} );
*/


/*Event Fire when page change*/
$(document).on("pagecontainerbeforetransition" || "pagebeforechange", function (e, data) {
    var to_page = data.toPage[0].id;
    //console.info("On page change: " + to_page);

    /* if(to_page == "mainpage")
    {
        add_todays_info_to_slider();
    }*/

    if (page_load_count == 1) {

        if (to_page == "tspecial") {
            add_loc_list("#select-loc");
            add_cat_list("#select-category");
            add_card_list("#select-card");
            filter_list_cat("all");
            reset_todays_page();
        }
        if (to_page == "listingpage") {
            add_loc_list("#list_select_loc");
            add_card_list("#list_select_card");
            add_cat_list("#list_select_category");
        }
        if (to_page == "search") {
            add_loc_list("#search_location");
            add_card_list("#search_card");
            add_cat_list("#search_category");
            reset_search();
        }
        if (to_page == "startupinfo") {
            add_cat_on_startup();
        }
        if (to_page == "search_result_pg") {
            add_loc_list("#search_pg_loc");
            add_card_list("#search_pg_card");
            add_cat_list("#search_pg_category");
        }

        if (to_page == "mapage") {
            //console.log("pagebeforechange event");
            /*   var height = $("body").innerHeight() - ($("#mainheader").height() + ($("#mainheader").height()*2));
        $('#map-canvas').height(height);

    alert($("body").innerHeight() + "  height: "+height +" header: "+$("#mapheader").height()+" navi: "+$("#mapnavi").height());
        
        initialize_map();*/
        }



        if (to_page == "dispmapg") {
            document.getElementById('disp_map_iframe').contentWindow.on_page_start();
            //console.log("After on_page_start called");
        }

        if (to_page == "currentmapg") {
            document.getElementById('current_map_iframe').contentWindow.current_opsition();
        }


        page_load_count = 0;
    } else {
        page_load_count++;

    }
    if (to_page == "show_shopping_list") {
        show_shp_list();
    }

});

function on_load() {
     alert("on_load");
     FastClick.attach(document.body);

        StatusBar.overlaysWebView(false);
        StatusBar.backgroundColorByHexString('#ffffff');
        StatusBar.styleDefault();

        navigator.splashscreen.hide();

    $.mobile.loading("show", {
        text: "Loading...",
        textVisible: "true",
        theme: "b",
    });

    var url = serviceUrl + 'adverts.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        adverts_data = myData.advrts;
        //console.info("Adverts");
        //console.log(adverts_data);
        adverts_load = true;
        add_adverts_to_slider();
        hide_loader();
    }, 'json');



    var url = serviceUrl + 'category.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        category_data = myData.category;
        //console.info("category");
        //console.log(category_data);
        addmenu();
        //category_load = true;
        //add_todays_info_to_slider();
        count_cat_for_slide();
        hide_loader();
    }, 'json');



    var url = serviceUrl + 'premiumoffers.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        premiumoffers_data = myData.premiumListing;
        //console.info("premiumoffers");
        //console.log(premiumoffers_data);
        premiumoffers_load = true;
        add_premium_list_to_slider();
        hide_loader();
    }, 'json');


    var url = serviceUrl + 'card_detail.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        //if (myData.card_details[0] == '') {
        //    myData.card_details = [];
        //}
        card_detail_data = myData.card_details;
        //console.info("card");
        //console.log(card_data);
        card_detail_load = true;
        add_todays_info_to_slider();
        hide_loader();
    }, 'json');


    var url = serviceUrl + 'cards.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        if (myData.card[0] == '') {
            myData.card = [];
        }
        card_data = myData.card;
        //console.info("card");
        //console.log(card_data);
        card_load = true;
        //add_todays_info_to_slider();
        //hide_loader();
    }, 'json');


    var url = serviceUrl + 'todayspecial.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        todayspecial_data = myData.todaysspecial;
        count_todays_for_slide();
        //console.info("todayspecial");
        //console.log(todayspecial_data);
        todayspecial_load = true;
        add_todays_info_to_slider();
        hide_loader();
    }, 'json');


    var url = serviceUrl + 'card_detail.php';
    $.post(url, function (myData) {
        card_detail_data = myData.card_details;
    }, 'json');

    /* $.getJSON('adslider.json', function (data) {
        var output = "";
        for (var i in data.ad) {
            output += '<li><img src="' + data.ad[i].src + '"  onclick="' + data.ad[i].changepage + '" alt="Picture" style="height: 24vh;width: 100vw;" /></li>';
        }
        //console.info("output: "+output);
        $('#adslider').append(output);

        $('#adslider').bxSlider({
            infiniteLoop: false,
            hideControlOnEnd: true,
            controls: false,
            adaptiveHeight: true,
        });

    });*/

    //}
    get_loc();

}

//Function for hide loder on start app after loading data from server
function hide_loader() {
    if (adverts_load && category_load && premiumoffers_load && card_detail_load && todayspecial_load) {
        $.mobile.loading("hide");
    }

}

/*Function for add category to menu*/
function addmenu() {
    var out_html = "";

    out_html = '<li><a href="#mainpage"></a></li><li><a href="#mainpage" onclick="$.mobile.changePage(\'#mainpage\')" >Home</a></li><li><a onclick="barcode_scan()" href="#" >Barcode</a></li><li><a href="#registerpage" onclick="$.mobile.changePage(\'#registerpage\')" >Sign Up</a></li><li><a href="#loginpage" onclick="$.mobile.changePage(\'#loginpage\')">Login</a></li><li><a onclick="settingpage()"  >Setting</a></li><li data-role="list-divider" id="menu_list_li_categories">Categories</li>';


    for (var i in category_data) {

        out_html += '<li><a href="#category_page" onclick="call_category_page(' + category_data[i].id + ',\'' + category_data[i].category_name + '\')"  >' + category_data[i].category_name + '</a></li>';
    }


    out_html += '<li data-role="list-divider">Other</li><li><a href="#fashion_pg" onclick="$.mobile.changePage(\'#fashion_pg\')"  >Fashion</a></li><li><a href="#mainpage" onclick="$.mobile.changePage(\'#mainpage\')"  >Point</a></li><li><a href="#mainpage" onclick="$.mobile.changePage(\'#mainpage\')"  >Discount</a></li><li><a href="#mainpage" onclick="$.mobile.changePage(\'#mainpage\')"  >Notification</a></li><li><a href="#mainpage"  >LBS</a></li><li><a href="#select_card_pg" onclick="$.mobile.changePage(\'#select_card_pg\')"  >All cards</a></li><li><a href="#mainpage"></a></li>';
    /*<li><a href="#dispmapg" onclick="$.mobile.changePage(\'#dispmapg\')"  >Map</a></li>*/
    $('#ul_menu').html(out_html);
}

function settingpage() {


    /* $( "#tabs" ).tabs();
    $("#tabs div ul .ui-tabs-active").addClass('ui-state-active');
    
    var nextIndex = $('.ui-tabs-active').index(this) + 1;
    $('.ui-tabs-active')[nextIndex].focus();*/

    var setup_info = {
        gender: "",
        ageGroup: "",
        maritalStatus: "",
        children: "",
        childAgeGroup: "",
        email: "",
        friendsEmail: "",
        card_cat: "",
        card_type: "",
        cards: "",
        catPref: "",
        alert: "",
        alertOptions: "",
        alertTime_start: "",
        alertTime_end: ""
    };

    var str_setup = localStorage.getItem('setup');
    setup_info = JSON.parse(str_setup);


    $('#txtgender').val(setup_info.gender);
    $('#txtageg').val(setup_info.ageGroup);
    $('#txtmaritalstatus').val(setup_info.maritalStatus);
    /* setup_info.children = $('input[name=radio-child-v-2]:checked', '#form_personal').val();
    var child_agegroup = [];
     $('#selectmul-childage :selected').each(function (i, selected) {
        child_agegroup[i] = $(selected).val();
    });
    
    
    $("#checkFirst").click(function(){
				$("input[type='radio']:first").attr("checked", "checked");
				$("input[type='radio']").checkboxradio("refresh");
			});
			$("#checkSecond").click(function(){
				$("input[type='radio']:eq(1)").attr("checked", "checked");
				$("input[type='radio']").checkboxradio("refresh");
			});
			$("#checkLast").click(function(){
				$("input[type='radio']:last").attr("checked", "checked");
				$("input[type='radio']").checkboxradio("refresh");
			});
			$("#uncheckAll").click(function(){
				$("input[type='radio'][checked]").removeAttr("checked");
				$("input[type='radio']").checkboxradio("refresh");
			});
    
    
    setup_info.childAgeGroup = child_agegroup;*/
    //console.info("child " + setup_info.children);
    if (setup_info.children == 'yes') {
        $("input[name=radio-child-v-2]:first", "#form_personal").attr("checked", "checked");
        //$("input[name=radio-child-v-2]", "#form_personal").checkboxradio("refresh");
        //console.info("child in if");

        for (var i in setup_info.childAgeGroup) {
            $('#selectmul-childage option[value=' + setup_info.childAgeGroup[i] + ']').attr('selected', 'selected');
        }
    } else {
        //console.info("child else");
        $("input[name=radio-child-v-2]:last", "#form_personal").attr("checked", "checked");
        //$("input[name=radio-child-v-2]", "#form_personal").checkboxradio("refresh");
    }

    $('#user_emai').val(setup_info.email);
    $('#friendemail').val(setup_info.friendsEmail.toString());


    setup_info.alert = $('input[name=radio-alert-v-2]:checked', '#from_alert').val();

    if (setup_info.alert == "on") {
        $("#radio-alert-h-2a").prop("checked", true).checkboxradio("refresh");

        // $("input[name=radio-alert-v-2]:first", "#from_alert").attr("checked", "checked");
    }
    if (setup_info.alert == "category") {
        //$("#radio-alert-h-2b", "#from_alert").attr("checked", "checked");
        $("#radio-alert-h-2b").prop("checked", true).checkboxradio("refresh");
    }
    if (setup_info.alert == "off") {
        //$("#radio-alert-h-2c", "#from_alert").attr("checked", "checked");
        $("#radio-alert-h-2c").prop("checked", true).checkboxradio("refresh");
    }


    if (setup_info.alertOptions == 'lunch') {
        //$("input[name=radio-alert-opt-h-2]:first", "#from_alert").attr("checked", "checked");
        $("#radio-alert-opt-h-2a").prop("checked", true).checkboxradio("refresh");
    }
    if (setup_info.alertOptions == 'dinner') {
        //$("#radio-alert-opt-h-2b", "#from_alert").attr("checked", "checked");
        $("#radio-alert-opt-h-2b").prop("checked", true).checkboxradio("refresh");
    }
    if (setup_info.alertOptions == 'party') {
        //$("#radio-alert-opt-h-2c", "#from_alert").attr("checked", "checked");
        $("#radio-alert-opt-h-2c").prop("checked", true).checkboxradio("refresh");
    }
    if (setup_info.alertOptions == 'time') {
        //$("#radio-alert-opt-h-2d", "#from_alert").attr("checked", "checked");
        $("#radio-alert-opt-h-2d").prop("checked", true).checkboxradio("refresh");
    }

    $("#time_start").val(setup_info.alertTime_start);
    $("#time_end").val(setup_info.alertTime_end);

    /*
    var realvalues = [];
    var textvalues = [];
    $('#selectmul-categorys :selected').each(function (i, selected) {
        realvalues[i] = $(selected).val();
        textvalues[i] = $(selected).text();
    });
    setup_info.catPref = realvalues;*/




    $.mobile.changePage('#startupinfo');
}

/*Function for call page to open for category*/
function call_category_page(cat_id, cat_name) {
    $('#cat_title').html(cat_name);

    /*search call*/
    //    if (all_data_load) {
    //        add_category_data(cat_id);
    //    } else {

    $.mobile.loading("show", {
        text: "Loading...",
        textVisible: "true",
        theme: "b",
    });





    var url = serviceUrl + 'category_data.php';
    $.ajax({
        type: "POST",
        url: url,
        data: {
            'category': cat_id
        },
        success: function (result) {

            //console.log("category_data");
            //console.info(result.listing_offer);
            select_category_list = result.listing_offer;
            add_category_data(cat_id);
            $.mobile.loading("hide");
        },
        failure: function (err) {
            $.mobile.loading("hide");
            //console.error("reg error: " + err);
        },
        dataType: "json"
    });




    /*    var data = {
            'keyword': "",
            'category': "",
            'card': "",
            'location': ""
        };

        
        var url = serviceUrl + 'search.php';


        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (result) {
                //console.info(result.status);
                $.mobile.loading("hide");
//                console.info(result);
                if (Object.keys(result.search_card).length || Object.keys(result.search_keyword_cat).length) {
                    console.warn("Ther is a resule");
                    all_data = result.search_keyword_cat;
                    all_data_load = true;
                    add_category_data(cat_id);
                } else {
                    //$('#popup_result_notfound').popup('open');
                }
            },
            failure: function (err) {
                console.error("reg error: " + err);
            },
            dataType: "json"
        });*/
    //    }

}

function add_category_data(cat_id) {

    //console.warn("cat_id: "+cat_id);
    var output = "";
    for (var i in select_category_list) {

        if (select_category_list[i].category_id == cat_id) {
            var img;
            if (select_category_list[i].default_image) {
                img = imageUrl + select_category_list[i].default_image;
            } else {
                img = "img/imageNotFound.jpg";
            }
            var dist = cal_dist(select_category_list[i].latitude, select_category_list[i].longitude);
            //console.info("in filter Distance from current pos: " + dist);
            /* var dist = (Math.random() * 5) + 1;*/
            dist = dist.toFixed(2);
            //convert km to m and concat unit after values
            if (dist < 1) {
                dist = (dist * 1000);
                dist = dist + "m";
            } else {
                dist = dist + "km";
            }

            output += "<div class='premium-item-box'><div class='premium-item-nice' data-option='" + select_category_list[i].id + "'><div class='premium-img-nice'><a href='#'  onclick='display_details(select_category_list[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + select_category_list[i].title + "</div><div class='premium-item-te-nice'>" + select_category_list[i].location + "</div><div class='premium-item-te-nice'>" + select_category_list[i].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + select_category_list[i].category_id + "</div><div class='premium-item-te-nice-1'>" + select_category_list[i].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";
        }
    }
    //    console.log(output);
    $('#div_cat_list').html(output);

    $(".premium-item-nice").bind("taphold", tapholdHandler);

    $.mobile.changePage('#category_page');

}


function count_todays_for_slide() {
    var obj = {};
    for (var i in category_data) {

        obj[category_data[i].id] = 0;
    }
    //console.warn(todayspecial_data);
    if (todayspecial_data) {
        if (Object.keys(todayspecial_data).length) {
            for (var i in todayspecial_data) {

                //console.log("id: " + todayspecial_data[i].id);
                //console.log("cat id: " + todayspecial_data[i].category_id);
                var cid;
                if (todayspecial_data[i].category_id) {
                    cid = todayspecial_data[i].category_id.split(",");
                } else {
                    cid = new Array();
                }


                for (var c = 0; c < cid.length; c++) {
                    // console.log("id: " + cid[c]);
                    // console.log("befor value: " + obj[cid[c]]);
                    obj[cid[c]] = (parseInt(obj[cid[c]]) + parseInt(1));

                    //console.log("after value: " + obj[cid[c]]);
                }

            }
        }
    }

    //console.info(obj);

    total_today_cat_count = obj;

}

//Function for counting list for each category
function count_cat_for_slide() {
    var obj = {},
        card_obj = {};
    for (var i in category_data) {

        obj[category_data[i].id] = 0;
    }
    for (var i in card_detail_data) {
        card_obj[card_detail_data[i].id] = 0;
    }

    //console.info(obj);


    var data = {
        'keyword': '',
        'category': '*',
        'card': '*',
        'location': ''
    };

    //console.info(data);
    var url = serviceUrl + 'search.php';


    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function (result) {
            //console.info(result.status);
            $.mobile.loading("hide");
            //console.info(result);
            if (Object.keys(result.search_card).length || Object.keys(result.search_keyword_cat).length) {

                //result.search_keyword_cat[i].category_id;
                for (var i in result.search_keyword_cat) {
                    //console.log( i + " ID:" + result.search_keyword_cat[i].id + " \t  Category ID:" + result.search_keyword_cat[i].category_id +" \t \t  Title: "+result.search_keyword_cat[i].title);

                    var cid, cardid;

                    if (result.search_keyword_cat[i].category_id) {
                        cid = result.search_keyword_cat[i].category_id.split(",");
                    } else {
                        cid = new Array();
                    }




                    for (var c = 0; c < cid.length; c++) {
                        //console.log("id: " + cid[c]);
                        // console.log("befor value: " + obj[cid[c]]);
                        obj[cid[c]] = (parseInt(obj[cid[c]]) + parseInt(1));
                        //console.log("after value: " + obj[cid[c]]);
                    }

                }
                //console.info(obj);

                total_list_cat_count = obj;
                category_load = true;
                add_todays_info_to_slider();
            } else {
                /*$('#popup_result_notfound').popup('open');*/
                total_list_cat_count = obj;
                category_load = true;
                add_todays_info_to_slider();
            }
        },
        failure: function (err) {
            console.error("reg error: " + err);
        },
        dataType: "json"
    });

}


/*Function to add todayspecial_data and other info in slide to main page*/
function add_todays_info_to_slider() {
    //console.warn("info_to_slider called");

    if (category_load && card_load && todayspecial_load) {
        //console.warn("in info to slider" + todayspecial_data);
        get_loc();
        var output = "";
        var total_loc = all_locations.length;

        output = '<div class="select-hading" onclick="$.mobile.changePage(\'#tspecial\'  );">Today’s Special<span>' + Object.keys(todayspecial_data).length + '</span></div>';

        /*output = "<li data-role='list-divider' data-theme='a' data-content-theme='a' role='heading' class='ui-li-divider ui-bar-a ui-li-has-count ui-first-child' style='text-align: -webkit-center;'><a onclick=\"$.mobile.changePage('#tspecial'  );\" class='ui-link' style='color: white;text-decoration-line: inherit;'>Today's Special<span class='ui-li-count ui-body-inherit'>" + Object.keys(todayspecial_data).length + "</span></a></li>";*/
        for (var i = 0; i < total_loc; i++) {
            var name = all_locations[i][0].split('_').join(' '); //replace "_" with space
            output += ' <div class="special-box"><div class="near-me">' + name + '</div><div class="near-tex">' + (10 + i) + '</div><div class="near-tex">' + (1 + i) + '</div></div>';
            //output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r' href='#" + name + "'><h2>" + name + "</h2><p class='ui-li-aside'><strong>" + (3 + i) + "</strong></p></a></li>";

        }

        //        output += '<div class="scan-btn"><a href="#"><img src="img/scan-btn.png" /></a></div>';

        $('#div_loc').html(output);
        $('#div_loc').listview();
        //$('#loc').listview(); //<-- here

        output = "";
        output = '<div class="select-hading" onclick="$.mobile.changePage(\'#tspecial\'  );">Today’s Special<span>' + Object.keys(todayspecial_data).length + '</span></div>';
        //        output = "<li data-role='list-divider' data-theme='a' data-content-theme='a' role='heading' class='ui-li-divider ui-bar-a ui-li-has-count ui-first-child' style='text-align: -webkit-center;'><a onclick=\"$.mobile.changePage('#tspecial'  );\" class='ui-link' style='color: white;text-decoration-line: inherit;'>Today's Special<span class='ui-li-count ui-body-inherit'>" + Object.keys(todayspecial_data).length + "</span></a></li>"
        /*for (var i in data.cat) {
            output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r'  href='" + data.cat[i].href + "'><h2>" + data.cat[i].name + "</h2><p class='ui-li-aside'><strong>" + data.cat[i].count + "</strong></p></a></li>";
        }*/
        for (var i in category_data) {
            //console.log("in category: "+category_data[i].id);
            var today_count = total_today_cat_count[category_data[i].id];

            if (today_count == "undefined" || today_count == "NaN") {
                today_count = 0;
            }

            output += ' <div class="special-box" ><div class="near-me" onclick="call_category_page(' + category_data[i].id + ',\'' + category_data[i].category_name + '\')" >' + category_data[i].category_name + '</div><div class="near-tex" onclick="call_category_page(' + category_data[i].id + ',\'' + category_data[i].category_name + '\')" >' + total_list_cat_count[category_data[i].id] + '</div><div class="near-tex" onclick="cat_todays_from_slider(' + category_data[i].id + ' )" >' + today_count + '</div></div>';
            //            output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r'  href='#" + category_data[i].id + "'><h2>" + category_data[i].category_name + "</h2><p class='ui-li-aside'><strong>" + (10 + i) + "</strong></p></a></li>";
        }

        //        output += '<div class="scan-btn"><a href="#"><img src="img/scan-btn.png" /></a></div>';

        $('#div_cat').html(output);
        $('#div_cat').listview();

        output = "";
        output = '<div class="select-hading" onclick="$.mobile.changePage(\'#tspecial\'  );">Today’s Special<span>' + Object.keys(todayspecial_data).length + '</span></div>';
        //output = "<li data-role='list-divider' data-theme='a' data-content-theme='a' role='heading' class='ui-li-divider ui-bar-a ui-li-has-count ui-first-child' style='text-align: -webkit-center;'><a onclick=\"$.mobile.changePage('#tspecial'  );\" class='ui-link' style='color: white;text-decoration-line: inherit;'>Today's Special<span class='ui-li-count ui-body-inherit'>" + Object.keys(todayspecial_data).length + "</span></a></li>"


        /*for (var i in data.card) {
            output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r'  href='" + data.card[i].href + "'><h2>" + data.card[i].name + "</h2><p class='ui-li-aside'><strong>" + data.card[i].count + "</strong></p></a></li>";
        }*/

        for (var i in card_detail_data) {
            output += ' <div class="special-box"><div class="near-me">' + card_detail_data[i].card_name + '</div><div class="near-tex">' + (2 + i) + '</div><div class="near-tex">' + (1 + i) + '</div></div>';
            //            output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r'  href='#" + card_data[i] + "'><h2>" + card_data[i] + "</h2><p class='ui-li-aside'><strong>" + (6 + i) + "</strong></p></a></li>";
        }

        //        output += '<div class="scan-btn"><a href="#"><img src="img/scan-btn.png" /></a></div>';

        $('#div_card').html(output);
        $('#div_card').listview();


        var output = "",
            list_name, total_shp_lst = 0;
        if (shopping_list) {
            list_name = _.keys(shopping_list);
            total_shp_lst = Object.keys(shopping_list).length;
        }
        output = '<div class="select-hading" onclick="$.mobile.changePage(\'#tspecial\'  );">Today’s Special<span>' + Object.keys(todayspecial_data).length + '</span></div>';

        /*output = "<li data-role='list-divider' data-theme='a' data-content-theme='a' role='heading' class='ui-li-divider ui-bar-a ui-li-has-count ui-first-child' style='text-align: -webkit-center;'><a onclick=\"$.mobile.changePage('#tspecial'  );\" class='ui-link' style='color: white;text-decoration-line: inherit;'>Today's Special<span class='ui-li-count ui-body-inherit'>" + Object.keys(todayspecial_data).length + "</span></a></li>";*/
        for (var i = 0; i < total_shp_lst; i++) {
            var name = list_name[i]; //replace "_" with space
            var tmp_list = shopping_list[name];
            output += ' <div class="special-box" onclick="$.mobile.changePage(\'#show_shopping_list\' );"><div class="near-me">' + name + '</div><div class="near-tex">' + tmp_list.length + '</div><div class="near-tex">' + " " + '</div></div>';
            //output += "<li><a class='ui-btn ui-btn-icon-right ui-icon-carat-r' href='#" + name + "'><h2>" + name + "</h2><p class='ui-li-aside'><strong>" + (3 + i) + "</strong></p></a></li>";

        }

        //        output += '<div class="scan-btn"><a href="#"><img src="img/scan-btn.png" /></a></div>';

        $('#div_shp_list').html(output);
        $('#div_shp_list').listview();





        $("#mainslider").listview();

        $('#mainslider').bxSlider({
            infiniteLoop: false,
            hideControlOnEnd: true,
            controls: false,
            adaptiveHeight: true,
            preloadImages: 'all',
            slideMargin: 2
        });

    }
}
/*End todayspecial_data function*/

//Function to display categorys todayspecial from slider 
function cat_todays_from_slider(cat_id) {
    filter_list_cat(cat_id);
    $.mobile.changePage('#tspecial');
    $('#select-category option[value=' + cat_id + ']').attr('selected', 'selected');
    $('#select-category').select('refresh', true);
    filter_list_cat($('#select-category').val());

}


/*Function for add premiumoffers info to slider on main page*/
function add_premium_list_to_slider() {
    var output = "";



    for (var i in premiumoffers_data) {
        //         output += '<li><ul data-role="listview"><li><a href="' + data.todays[i].href + '"><img src="' + data.todays[i].img + '"><div class="ui-grid-a"><div class="ui-block-a"><div class="ui-bar" style="height:60px"><h2>' + data.todays[i].name + '</h2><p>' + data.todays[i].place + '</p><p>' + data.todays[i].cards + '</p></div></div><div class="ui-block-b"><div class="ui-bar" style="height:60px"><h3 style="text-align: -webkit-right;">' + data.todays[i].category + '</h3><p style="text-align: -webkit-right;">' + data.todays[i].offer + '</p><p style="text-align: -webkit-right;">' + data.todays[i].dist + '</p></div></div></div></a></li></ul></li>';
        var img;
        if (premiumoffers_data[i].default_image) {
            img = imageUrl + premiumoffers_data[i].default_image;
        } else {
            img = "img/imageNotFound.jpg";
        }
        output += '<li><ul data-role="listview" style="list-style-type: none;padding: 0 0 0 0px;margin: 0px;"><li><article><div class="select-main-box-pro"><div class="premium-h">Premium <span>Listing</span></div><div class="premium-item"><div class="premium-img"><a onclick="display_details(premiumoffers_data[' + i + '])" href="#"><img src="' + img + '" /></a></div><div class="premium-item-text">' + premiumoffers_data[i].title + '</div><div class="premium-item-te">' + premiumoffers_data[i].offer_text + '<br />' + premiumoffers_data[i].card + '</div></div><div class="right-text">' + premiumoffers_data[i].location + '</div></div></article></li></ul></li>';

        if (i > 8) {
            break;
        }

        //        output += '<li style="float: left; list-style: none; position: relative; width: 1020px;"><ul data-role="listview" class="ui-listview"><li class="ui-li-has-thumb ui-first-child ui-last-child"><a onclick="display_details(premiumoffers_data[' + i + '])" href="#" class="ui-btn ui-btn-icon-right ui-icon-carat-r"><img src="' + img + '"><div class="ui-grid-a"><div class="ui-block-a"><div class="ui-bar" style="height:60px"><h2>' + premiumoffers_data[i].title + '</h2><p>' + premiumoffers_data[i].location + '</p><p>' + premiumoffers_data[i].card + '</p></div></div><div class="ui-block-b"><div class="ui-bar" style="height:60px"><h3 style="text-align: -webkit-right;">' + premiumoffers_data[i].category_id + '</h3><p style="text-align: -webkit-right;">' + premiumoffers_data[i].title + '</p><p style="text-align: -webkit-right;">' + premiumoffers_data[i].offer_text + '</p></div></div></div></a></li></ul></li>';

    }
    //console.info("output: "+output);
    $('#preslider').html(output);

    $('#preslider').bxSlider({
        infiniteLoop: false,
        hideControlOnEnd: true,
        controls: false,
        adaptiveHeight: true,
    });

}
/*End add_premium_list_to_slider function*/

/*Function to add Adverts info to slider on main page*/
function add_adverts_to_slider() {
    var output = "";
    for (var i in adverts_data) {
        var img;
        if (adverts_data[i].offer_image) {
            img = imageUrl + adverts_data[i].offer_image;
        } else {
            img = "img/imageNotFound.jpg";
        }


        output += '<li><img src="' + img + '"  onclick="adverts_details();" alt="Picture" style="height: 24vh;width: 100vw;" /></li>';

        if (i > 8) {
            break;
        }
    }


    //console.info("output: "+output);
    $('#adslider').html(output);


    $('#adslider').bxSlider({
        infiniteLoop: true,
        auto: false,
        autoControls: true,
        hideControlOnEnd: true,
        controls: false,
        adaptiveHeight: false
    });


    //    console.log("out_htm: "+ out_htm);
    //    $('#ad_slider').append(out_htm);
    //   
    //    $('#ad_slider').bxSlider({
    //        infiniteLoop: false,
    //        hideControlOnEnd: true,
    //        controls: false,
    //        adaptiveHeight: true
    //    });
}
/*End add_adverts_to_slider Function*/


function adverts_details() {

    $('#adspopup').popup('open', {
        overlayTheme: 'a',
        corners: true,
        positionTo: 'window'
    });


}


/*Function to inject comman menu in app*/
function injectMenu() {
    //$('.inner').append('<p>Test</p>');

    var pagelist = ["#mainpage", "#tspecial"];

    for (var i = 0, p_len = pagelist.length; i < p_len; i++) {
        $("<div class='ui-bar-b' data-role='panel' id='menuPanel'><ul data-role='listview' data-inset='true' data-divider-theme='b'<li><a href='#mainpage'>Home</a></li><li><a onclick='scanCode()' href='#'>Barcode</a></li><li><a href='#'>Sign Up</a></li><li><a href='#'>Login</a></li><li data-role='list-divider'>categories</li><li><a href='#'>Womens</a></li><li><a href='#'>Mens</a></li><li><a href='#'>Features</a></li><li><a href='#'>Brands</a></li><li data-role='list-divider'>Other</li><li><a href='#'>Point</a></li><li><a href='#tspecial'>Discount</a></li><li><a href='#'>Notification</a></li><li><a href='#'>LBS</a></li><li><a href='#'>All cards</a></li></ul></div>")
            .appendTo("'" + pagelist[i] + "'");
    }
    alert("after injectMenu");
}

/*
function onMenuKeyDown() {
                show_popup();
            }
*/
/*
function backKeyDown(){
			alert('Back button pressed.');
			}
			
function searchKeyDown(){
			alert('Search button pressed.');
			}
*/

function create_footer() {
    /*
    $( "<div data-role='footer' data-tap-toggle='true' data-position-fixed='true' data-theme='a'><div data-role='navbar'><ul><li><a href='#mainpage' data-icon='home'></a></li><li><a href='#' data-icon='eye'></a></li><li><a href='#' data-icon='plus'></a></li><li><a href='#' data-icon='location'></a></li><li><a onclick='scanCode()' href='#' data-icon='grid'></a></li></ul></div></div>")
        .appendTo( "#hbody" )
        .toolbar({ position: "fixed" });
    // Update the page height and padding
    $.mobile.resetActivePageHeight();*/
}

function show_popup() {

    /* $( "<div data-role='footer'><h4>Dynamic footer</h4></div>")
        .appendTo( "#hbody" )
        .toolbar({ position: "fixed" });*/
    // Update the page height and padding
    /*   $.mobile.resetActivePageHeight();*/


    var options = {
        transition: "slideup",
        /*      positionTo : "#p_listing"*/
    };
    $("#transitionExample").popup("open", options);
}


function barcode_scan() {
    cordova.plugins.barcodeScanner.scan(
        function (result) {

            if (result.cancelled) {
                alert("Scan Cancelled: " + result.cancelled);
            } else {
                alert("We got a barcode\n" +
                    "Result: " + result.text + "\n" +
                    "Format: " + result.format);
            }
        },
        function (error) {
            alert("Scanning failed: " + error);
        }
    );
}



var scanCode = function () {
    cordova.plugins.barcodeScanner.scan(
        function (result) {
            alert("Scanned Code: " + result.text + ". Format: " + result.format + ". Cancelled: " + result.cancelled);
        }, function (error) {
            alert("Scan failed: " + error);
        });
};

var encodeText = function () {
    window.plugins.barcodeScanner.encode(
        BarcodeScanner.Encode.TEXT_TYPE,
        "http://www.mobiledevelopersolutions.com",
        function (success) {
            alert("Encode success: " + success);
        }, function (fail) {
            alert("Encoding failed: " + fail);
        });
};

var encodeEmail = function () {
    window.plugins.barcodeScanner.encode(
        BarcodeScanner.Encode.EMAIL_TYPE,
        "a.name@gmail.com", function (success) {
            alert("Encode success: " + success);
        }, function (fail) {
            alert("Encoding failed: " + fail);
        });
};

var encodePhone = function () {
    window.plugins.barcodeScanner.encode(
        BarcodeScanner.Encode.PHONE_TYPE,
        "555-227-5283", function (success) {
            alert("Encode success: " + success);
        }, function (fail) {
            alert("Encoding failed: " + fail);
        });
};

var encodeSMS = function () {
    window.plugins.barcodeScanner.encode(
        BarcodeScanner.Encode.SMS_TYPE,
        "An important message for someone.", function (success) {
            alert("Encode success: " + success);
        }, function (fail) {
            alert("Encoding failed: " + fail);
        });
};


function getposition() {
    // onSuccess Callback
    // This method accepts a Position object, which contains the
    // current GPS coordinates
    //
    var onSuccess = function (position) {
        /*alert('Latitude: ' + position.coords.latitude + '\n' +
            'Longitude: ' + position.coords.longitude + '\n' +
            'Altitude: ' + position.coords.altitude + '\n' +
            'Accuracy: ' + position.coords.accuracy + '\n' +
            'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
            'Heading: ' + position.coords.heading + '\n' +
            'Speed: ' + position.coords.speed + '\n' +
            'Timestamp: ' + position.timestamp + '\n');*/

        var current_location = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        localStorage.setItem('current_pos', current_location);
    };

    // onError Callback receives a PositionError object
    //
    function onError(error) {
        alert('code: ' + error.code + '\n' +
            'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}

function save_current_loc() {
    var onSuccess = function (position) {
        current_lat = position.coords.latitude;
        current_lun = position.coords.longitude;
    };

    // onError Callback receives a PositionError object
    //
    function onError(error) {
        alert('code: ' + error.code + '\n' +
            'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}


function add_loc_getposition() {
    // onSuccess Callback
    // This method accepts a Position object, which contains the
    // current GPS coordinates
    var onSuccess = function (position) {
        var tmp_LatLng = '(' + position.coords.latitude + ', ' + position.coords.longitude + ')'; /*localStorage.getItem("tmp_LatLng");*/
        //console.info("Tmp Location: " + tmp_LatLng);
        localStorage.setItem('tmp_LatLng', tmp_LatLng);
        $('#loc_latlng').val(tmp_LatLng);
    };

    // onError Callback receives a PositionError object
    //
    function onError(error) {
        alert('code: ' + error.code + '\n' +
            'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}

/*Function for save location to localStorage*/
function save_loc() {
    var name_loc = $('#loc_name').val(),
        pos = localStorage.getItem("tmp_LatLng"),
        obj = {};

    if (localStorage.getItem("locations")) {
        obj = localStorage.getItem("locations");
        obj = JSON.parse(obj);
    }

    var name = name_loc.split(' ').join('_'); //replace space with "_"


    obj[name] = pos;
    //console.warn(obj);
    obj = JSON.stringify(obj);
    //console.info(obj);
    localStorage.setItem('locations', obj);
    reset_loc();
    get_loc();
}

/*Function for get all location from localstorage*/
function get_loc() {
    var tmp_loc = JSON.parse(localStorage.getItem("locations"));

    all_locations = _.pairs(tmp_loc);

    //console.info("All Locations: " + all_locations);
}

/*Function for reset value to All New Location Form*/
function reset_loc() {
    $('#loc_name').val("");
    $('#loc_latlng').val("");
    $('#loc_slider').val('map').slider("refresh");
    $('#loc_slider').change();
    //console.info(category_data);
}

function add_location_temp() {
    var temp_LatLng = localStorage.getItem("current_pos");
    localStorage.setItem("tmp_LatLng", temp_LatLng);
    //$('#loc_name').val(localStorage.getItem("tmp_loc_name"));
    $('#loc_latlng').val(temp_LatLng);
}

/*Function to move next tab*/
function nexttab() {
    var active_tab = $("#tabs").tabs("option", "active");
    active_tab++;
    $('#tabs').tabs({
        active: active_tab
    });
    var tabli = $('#tabs ul li[tabindex=' + active_tab + ']');
    var tabLink = $('#tabs ul li[tabindex=' + active_tab + '] a');

    //tabli.aria-selected("true");

    /* tabli.addClass('ui-state-active');
    tabLink.addClass();*/
}
//Function for prvious tab
function pretab() {
    var active_tab = $("#tabs").tabs("option", "active");
    active_tab--;
    $('#tabs').tabs({
        active: active_tab
    });
    var tabli = $('#tabs ul li[tabindex=' + active_tab + ']');
    var tabLink = $('#tabs ul li[tabindex=' + active_tab + '] a');

    //tabli.aria-selected("true");

    /* tabli.addClass('ui-state-active');
    tabLink.addClass();*/
}

/*Function to set home location and store to localStorage*/
function set_home_location() {
    var home_LatLng = localStorage.getItem("current_pos"),
        obj = {};
    //console.info("Home Location: " + home_LatLng);
    localStorage.setItem('home_location', home_LatLng);
    //obj["Home"] = home_LatLng;
    //obj = JSON.stringify(obj);
    //localStorage.setItem('locations', obj);
}

function radio_alert_option(opt) {
    if (opt == "time") {
        $("input[name='time_slot']").textinput('enable');
    } else {
        $("input[name='time_slot']").textinput('disable');
    }
}

function radio_alert(valu) {
    if (valu == "off") {
        $("input[name='radio-alert-opt-h-2']").checkboxradio('disable');
        $("input[name='time_slot']").textinput('disable');
    } else {
        $("input[name='radio-alert-opt-h-2']").checkboxradio('enable');
    }
}

function radiochi(tf) {

    //console.info("value of radiobtn"+tf);
    if (tf) {
        $('#selectmul-childage').selectmenu("enable");
        /*$('#childage-1a').checkboxradio("enable");
        $('#childage-2a').checkboxradio("enable");
        $('#childage-3a').checkboxradio("enable");
        $('#childage-4a').checkboxradio("enable");
        $('#childage-5a').checkboxradio("enable");
        $('#childage-6a').checkboxradio("enable");*/
    } else {
        $('#selectmul-childage').selectmenu("disable");
        /*$('#childage-1a').checkboxradio("disable");
        $('#childage-2a').checkboxradio("disable");
        $('#childage-3a').checkboxradio("disable");
        $('#childage-4a').checkboxradio("disable");
        $('#childage-5a').checkboxradio("disable");
        $('#childage-6a').checkboxradio("disable");*/
    }
}

/*Function for save setup data to localStorage*/
function save_setup() {
    var setup_info = {
        gender: "",
        ageGroup: "",
        maritalStatus: "",
        children: "",
        childAgeGroup: "",
        email: "",
        friendsEmail: "",
        card_cat: "",
        card_type: "",
        cards: "",
        catPref: "",
        alert: "",
        alertOptions: "",
        alertTime_start: "",
        alertTime_end: ""
    };

    setup_info.gender = $('#txtgender').val();
    setup_info.ageGroup = $('#txtageg').val();
    setup_info.maritalStatus = $('#txtmaritalstatus').val();
    setup_info.children = $('input[name=radio-child-v-2]:checked', '#form_personal').val();
    var child_agegroup = [];
    $('#selectmul-childage :selected').each(function (i, selected) {
        child_agegroup[i] = $(selected).val();
    });

    setup_info.childAgeGroup = child_agegroup;

    setup_info.email = $('#user_emai').val();
    setup_info.friendsEmail = $('#friendemail').val().split(",");

    var realvalues = [];
    var textvalues = [];
    $('#selectmul-categorys :selected').each(function (i, selected) {
        realvalues[i] = $(selected).val();
        textvalues[i] = $(selected).text();
    });
    setup_info.catPref = realvalues;


    setup_info.card_cat = $('#drop_card_cat').val();
    setup_info.card_type = $('#drop_card_type').val();

    var cardid = [];
    $('#selectmul_cards :selected').each(function (i, selected) {
        cardid[i] = $(selected).val();
        //textvalues[i] = $(selected).text();
    });
    setup_info.cards = cardid;

    setup_info.alert = $('input[name=radio-alert-v-2]:checked', '#from_alert').val();

    if ($('input[name=radio-alert-v-2]:checked', '#from_alert').val() == "off") {
        setup_info.alertOptions = "none";
    } else {
        setup_info.alertOptions = $('input[name=radio-alert-opt-h-2]:checked', '#from_alert').val();
        if ($('input[name=radio-alert-opt-h-2]:checked', '#from_alert').val() == "time") {
            setup_info.alertTime_start = $("#time_start").val();
            setup_info.alertTime_end = $("#time_end").val();
        } else {
            setup_info.alertTime_start = 0;
            setup_info.alertTime_end = 0;
        }
    }
    //setup_info.alert = $('input[name=radio-alert-v-2]:checked', '#from_alert').val();
    var str_setup = JSON.stringify(setup_info);
    localStorage.setItem('setup', str_setup);
    localStorage.setItem('user_profile', 1);
    set_home_location();
    $.mobile.changePage('#mainpage');
    location.reload(true);
}

/*Function for update all data from server*/
function update_data() {

    /* $.mobile.changePage(
        window.location.href, {
            allowSamePageTransition: true,
            transition: 'none',
            showLoadMsg: true,
            reloadPage: true
        }
    );*/

    $.mobile.loading("show", {
        text: "Loading...",
        textVisible: "true",
        theme: "b",
    });

    adverts_load = false;
    category_load = false;
    premiumoffers_load = false;
    card_load = false;
    todayspecial_load = false;

    var url = serviceUrl + 'adverts.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        adverts_data = myData.advrts;
        //console.info("Adverts");
        //console.log(adverts_data);
        adverts_load = true;
        //add_adverts_to_slider();
        hide_loader_update();
    }, 'json');



    var url = serviceUrl + 'category.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        //category_data = myData.category;
        //console.info("category");
        //console.log(category_data);
        category_load = true;
        //add_todays_info_to_slider();
        //addmenu();
        hide_loader_update();
    }, 'json');



    var url = serviceUrl + 'premiumoffers.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        premiumoffers_data = myData.premiumListing;
        //console.info("premiumoffers");
        //console.log(premiumoffers_data);
        premiumoffers_load = true;
        //add_premium_list_to_slider();
        hide_loader_update();
    }, 'json');


    var url = serviceUrl + 'cards.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        if (myData.card[0] == '') {
            myData.card = [];
        }
        card_data = myData.card;
        //console.info("card");
        //console.log(card_data);
        card_load = true;
        //add_todays_info_to_slider();
        hide_loader_update();
    }, 'json');


    var url = serviceUrl + 'todayspecial.php';

    $.post(url, function (myData) {
        //console.warn(myData);
        todayspecial_data = myData.todaysspecial;
        //console.info("todayspecial");
        //console.log(todayspecial_data);
        todayspecial_load = true;
        //add_todays_info_to_slider();
        hide_loader_update();
    }, 'json');

    var url = serviceUrl + 'card_detail.php';
    $.post(url, function (myData) {
        //console.info(myData);
        card_detail_data = myData.card_details;
    }, 'json');


    shopping_list = JSON.parse(localStorage.getItem("shopping_list"));

    get_loc();

    $.mobile.changePage("#mainpage");
}

function hide_loader_update() {
    if (adverts_load && category_load && premiumoffers_load && card_load && todayspecial_load) {
        $.mobile.loading("hide");
    }

}

/*Function for refreshPage witout blink*/
function refreshPage() {
    $.mobile.changePage(
        window.location.href, {
            allowSamePageTransition: true,
            transition: 'none',
            showLoadMsg: false,
            reloadPage: true
        }
    );
}


/*Function for add location to dropdown list*/
function add_loc_list(list_id) {
    var total_loc = all_locations.length,
        html_out = '<option value="all">All</option>';

    for (var i = 0; i < total_loc; i++) {
        var name = all_locations[i][0].split('_').join(' '); //replace "_" with space
        //console.info("Location Name: "+name+" LatLng: "+all_locations[i][1]);
        html_out += '<option value="' + i + '">' + name + '</option>';
    }
    $(list_id).html(html_out).select("refresh");

    //console.info("Option html: "+html_out);

    /*<select id="select-loc" name="select-loc" data-shadow="false" data-mini="true">
                    </select>*/

}

function add_card_list(list_id) {

    var total_card = Object.keys(card_data).length,
        html_out = '<option value="all">All</option>';
    for (var i = 0; i < total_card; i++) {
        /*var cat_name = category_data[i].category_name;*/
        /*console.info("cat name: "+cat_name);*/
        html_out += '<option value="' + card_data[i] + '">' + card_data[i] + '</option>';
    }

    //console.info(html_out);
    $(list_id).html(html_out).select("refresh");

}

/*Function for add categorys to dropdown list*/
function add_cat_list(list_id) {

    var total_cat = Object.keys(category_data).length,
        html_out = '<option value="all">All</option>';

    //console.info(list_id +" len: " + total_cat +" category_data: "+category_data.category);
    for (var i = 0; i < total_cat; i++) {
        /*var cat_name = category_data[i].category_name;*/
        /*console.info("cat name: "+cat_name);*/
        html_out += '<option value="' + category_data[i].id + '">' + category_data[i].category_name + '</option>';
    }

    //console.info(html_out);
    $(list_id).html(html_out).select("refresh");



}

function add_cat_on_startup() {

    add_card_info_on_startup();

    var url = serviceUrl + 'category.php';

    var setup_info = {
        gender: "",
        ageGroup: "",
        maritalStatus: "",
        children: "",
        childAgeGroup: "",
        email: "",
        friendsEmail: "",
        card_cat: "",
        card_type: "",
        cards: "",
        catPref: "",
        alert: "",
        alertOptions: "",
        alertTime_start: "",
        alertTime_end: ""
    };

    var str_setup = localStorage.getItem('setup');
    if (JSON.parse(str_setup)) {
        setup_info = JSON.parse(str_setup);
    }


    $.post(url, function (myData) {
        var html_out = '<option>Choose options</option>',
            html_li = '<li data-option-index="0" data-icon="false" data-placeholder="true" class="ui-screen-hidden" role="option" aria-selected="false"><a href="#" tabindex="-1" class="ui-btn ui-checkbox-off ui-btn-icon-right">Choose options</a></li>';
        //console.info(myData.category);
        for (var i in myData.category) {

            /*var cat_name = category_data[i].category_name;*/
            /*console.info("cat name: "+cat_name);*/
            html_out += '<option value="' + myData.category[i].id + '">' + myData.category[i].category_name + '</option>';
            html_li += '<li data-option-index="' + (parseInt(i) + parseInt(1)) + '" data-icon="false" class="ui-first-child" role="option" aria-selected="false"><a href="#" tabindex="-1" class="ui-btn ui-btn-icon-right ui-checkbox-off">' + myData.category[i].category_name + '</a></li>';

        }

        $('div.formspace #v_state').append('<option selected="selected" value="TX">Texas</option>')
            .selectmenu("refresh");
        //console.info(html_out);
        $("#four form div #selectmul-categorys").html(html_out).select("refresh");

        $("#selectmul-categorys-menu").html(html_li);

        for (var i in setup_info.catPref) {
            //console.info("cat pref: " + setup_info.catPref[i]);
            $('#selectmul-categorys option[value=' + setup_info.catPref[i] + ']').attr('selected', 'selected');
        }

        $('#selectmul-categorys').selectmenu('refresh', true);


        $('#drop_card_cat').val(setup_info.card_cat);
        $('#drop_card_type').val(setup_info.card_type);
        card_list(setup_info.card_cat, setup_info.card_type);

        for (var i in setup_info.cards) {
            //console.info("cat pref: " + setup_info.catPref[i]);
            $('#selectmul_cards option[value=' + setup_info.cards[i] + ']').attr('selected', 'selected');
        }

        $('#selectmul_cards').selectmenu('refresh', true);


        //console.warn($("#selectmul-categorys").html);
    }, 'json');

}


function add_card_info_on_startup() {

    var url = serviceUrl + 'card_detail.php';

    $.post(url, function (myData) {

        card_detail_data = myData.card_details;
        var total_card_category = [],
            total_card_type = [],
            img = [];
        var html_out = "";
        for (var i in card_detail_data) {
            total_card_category.push(card_detail_data[i].card_category_title);
            total_card_type.push(card_detail_data[i].card_type_title);

        }
        total_card_category = _.uniq(total_card_category);
        total_card_type = _.uniq(total_card_type);

        for (var c in total_card_category) {
            //console.info(total_card_category[c]);
            html_out += '<option value="' + total_card_category[c] + '">' + total_card_category[c] + '</option>';
        }

        $("#drop_card_cat").html(html_out).select("refresh");

        html_out = "";
        for (var j in total_card_type) {

            html_out += '<option value="' + total_card_type[j] + '">' + total_card_type[j] + '</option>';
        }
        $("#drop_card_type").html(html_out).select("refresh");

        $("#drop_card_cat").change(function () {
            card_list($('#drop_card_cat').val(), $('#drop_card_type').val());
            //        console.log("in bind change select");
        });
        $("#drop_card_type").change(function () {
            card_list($('#drop_card_cat').val(), $('#drop_card_type').val());
            //        console.log("in bind change select");
        });




    }, 'json');

}

function card_list(ccat, ctype) {
    var html_out = '<option>Choose options</option>',
        html_li = '<li data-option-index="0" data-icon="false" data-placeholder="true" class="ui-screen-hidden" role="option" aria-selected="false"><a href="#" tabindex="-1" class="ui-btn ui-checkbox-off ui-btn-icon-right">Choose Card</a></li>';

    //console.info(" ccat: "+ccat);
    for (var i in card_detail_data) {
        if (card_detail_data[i].card_category_title == ccat && card_detail_data[i].card_type_title == ctype) {
            var img;
            if (card_detail_data[i].card_image) {
                img = imageUrl + card_detail_data[i].card_image;
            } else {
                img = "img/imageNotFound.jpg";
            }

            //console.info("ctype: "+ctype+" ccat: "+ccat+" cadr name: "+card_detail_data[i].card_name);
            html_out += '<option value="' + card_detail_data[i].id + '">' + card_detail_data[i].card_name + '</option>';
            html_li += '<li data-option-index="' + (parseInt(i) + parseInt(1)) + '" class="ui-first-child" role="option" aria-selected="false"><a href="#" tabindex="-1" class="ui-btn ui-btn-icon-right ui-checkbox-off">' + card_detail_data[i].card_name + '</a></li>'; //data-icon="false"
            //console.warn(img);

        }
    }

    // $('div.formspace #v_state').append('<option selected="selected" value="TX">Texas</option>').selectmenu("refresh");
    //console.info(html_out);
    $("#three form div #selectmul_cards").html(html_out).select("refresh");

    $("#selectmul_cards-menu").html(html_li);

    $('#selectmul_cards').selectmenu('refresh', true);

}


function reset_todays_page() {

    $('#select-category').val("all");
    //$('#select-category').selectmenu("refresh");
    $('#select-card').val("all");
    //$('#select-card').selectmenu("refresh");
    $('#select-loc').val("all");
    //$('#select-loc').selectmenu("refresh");
    $('#slider').val(0);
}



/*Function to cal. radius to deg.*/
function rad2deg(angle) {
    return angle * 57.29577951308232; // angle / Math.PI * 180
}

/*Function to cal deg to radius*/
function deg2rad(angle) {
    return angle * .017453292519943295; // (angle / 180) * Math.PI;
}

/*Function to chacke given lat-lug is in given Range or not*/
function is_in_radius(lat, lug, max_lat, min_lat, max_lug, min_lug) {

    if (min_lat < lat && max_lat > lat) {

        if (min_lug < lug && max_lug > lug) {
            return true
        } else {
            return false
        }
    } else {
        return false
    }
}


/*Function to cal max and min lat lug to cal radius*/
//function in_radius(lat,lng,distance)
function maxmin_latlun() {
    // we'll want everything within, say, 10km distance
    //var distance = 10;
    var distance = 6,
        lat = current_lat,
        lng = current_lun;

    // earth's radius in km = ~6371
    var radius = 6371;

    // latitude boundaries
    var max_lat = lat + rad2deg(distance / radius);
    var min_lat = lat - rad2deg(distance / radius);

    // longitude boundaries (longitude gets smaller when latitude increases)
    var max_lng = lng + rad2deg(distance / radius / Math.cos(deg2rad(lat)));
    var min_lng = lng - rad2deg(distance / radius / Math.cos(deg2rad(lat)));

    var mm_latlun = {
        maxlat: max_lat,
        minlat: min_lat,
        maxlng: max_lng,
        minlng: min_lng
    };

    //console.info("maxlat: " + mm_latlun.maxlat + " minlat: " + mm_latlun.minlat + " maxlng: " + mm_latlun.maxlng + " minlng: " + mm_latlun.minlng);

    return mm_latlun
}

/*Function to calculate distance between places*/
function cal_dist(place_lat, place_lun) {

    var lat1 = current_lat,
        lon1 = current_lun,
        lat2 = place_lat,
        lon2 = place_lun,
        unit = "K";

    //        console.log("lat1: " + lat1 + " lon1: " + lon1 + " lat2: " + lat2 + " lon2: " + lon2);

    var radlat1 = Math.PI * lat1 / 180

    var radlat2 = Math.PI * lat2 / 180

    var radlon1 = Math.PI * lon1 / 180

    var radlon2 = Math.PI * lon2 / 180

    var theta = lon1 - lon2

    var radtheta = Math.PI * theta / 180

    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);

    dist = Math.acos(dist)

    dist = dist * 180 / Math.PI

    dist = dist * 60 * 1.1515

    if (unit == "K") {
        dist = dist * 1.609344
    }

    if (unit == "N") {
        dist = dist * 0.8684
    }

    //console.info("in dist_fun Distance from current pos: " + dist);

    return dist

}



//////////////////////////////////////////////////New dinamic out put for list//////////////////////////////////////
/*
output +="<div class='premium-item-box'><div class='premium-item-nice'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img src='" + img + "' /></a></div><div class='canter-taxt'><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1'><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice'>" + dist + "</div></div>";
*/


//Function for filter list from category
function filter_list_cat(fil) {
    // fil = fil.toLowerCase();
    var output = "";

    if (fil) {
        if (fil == "all" || fil == "ALL") {
            for (var i in todayspecial_data) {
                var img;
                if (todayspecial_data[i].default_image) {
                    img = imageUrl + todayspecial_data[i].default_image;
                } else {
                    img = "img/imageNotFound.jpg";
                }

                var dist = cal_dist(todayspecial_data[i].latitude, todayspecial_data[i].longitude);
                //console.info("in filter Distance from current pos: " + dist);
                /* var dist = (Math.random() * 5) + 1;*/
                dist = dist.toFixed(2);
                //convert km to m and concat unit after values
                if (dist < 1) {
                    dist = (dist * 1000);
                    dist = dist + "m";
                } else {
                    dist = dist + "km";
                }


                output += "<div class='premium-item-box'><div class='premium-item-nice' data-option='" + todayspecial_data[i].id + "'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";

                // output += "<li><a onclick='display_details(todayspecial_data[" + i + "])' href='#'> <img src='" + img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + todayspecial_data[i].title + "</h2> <p>" + todayspecial_data[i].location + "</p> <p>" + todayspecial_data[i].card + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + todayspecial_data[i].category_id + "</h3><p style='text-align: -webkit-right;'>" + todayspecial_data[i].offer_text + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
            }
        } else {
            for (var i in todayspecial_data) {
                if (todayspecial_data[i].category_id == fil) {
                    var img;
                    if (todayspecial_data[i].default_image) {
                        img = imageUrl + todayspecial_data[i].default_image;
                    } else {
                        img = "img/imageNotFound.jpg";
                    }
                    var dist = cal_dist(todayspecial_data[i].latitude, todayspecial_data[i].longitude);
                    //console.info("in filter Distance from current pos: " + dist);
                    /* var dist = (Math.random() * 5) + 1;*/
                    dist = dist.toFixed(2);
                    //convert km to m and concat unit after values
                    if (dist < 1) {
                        dist = (dist * 1000);
                        dist = dist + "m";
                    } else {
                        dist = dist + "km";
                    }

                    output += "<div class='premium-item-box'><div class='premium-item-nice' data-option='" + todayspecial_data[i].id + "'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";
                    //                    output += "<li><a onclick='display_details(todayspecial_data[" + i + "])' href='#'> <img src='" + img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + todayspecial_data[i].title + "</h2> <p>" + todayspecial_data[i].location + "</p> <p>" + todayspecial_data[i].card + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + todayspecial_data[i].category_id + "</h3><p style='text-align: -webkit-right;'>" + todayspecial_data[i].offer_text + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                }
            }
        }
    } else {
        for (var i in todayspecial_data) {
            var img;
            if (todayspecial_data[i].default_image) {
                img = imageUrl + todayspecial_data[i].default_image;
            } else {
                img = "img/imageNotFound.jpg";
            }
            var dist = cal_dist(todayspecial_data[i].latitude, todayspecial_data[i].longitude);
            //console.info("in filter Distance from current pos: " + dist);
            /* var dist = (Math.random() * 5) + 1;*/
            dist = dist.toFixed(2);
            //convert km to m and concat unit after values
            if (dist < 1) {
                dist = (dist * 1000);
                dist = dist + "m";
            } else {
                dist = dist + "km";
            }

            output += "<div class='premium-item-box'><div class='premium-item-nice' data-option='" + todayspecial_data[i].id + "'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";
            //            output += "<li><a onclick='display_details(todayspecial_data[" + i + "])' href='#'> <img src='" + img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + todayspecial_data[i].title + "</h2> <p>" + todayspecial_data[i].location + "</p> <p>" + todayspecial_data[i].card + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + todayspecial_data[i].category_id + "</h3><p style='text-align: -webkit-right;'>" + todayspecial_data[i].offer_text + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
        }
    }
    //    $("#ListDiv").html('<ul id="List" data-role="listview"></ul>');
    //    $('#div_List').append(output);
    $('#div_List').html(output);
    //    $('#List').listview(); //<-- here

    //For bind TapHold on this list
    $(".premium-item-nice").bind("taphold", tapholdHandler);


    /*
    $.getJSON('todays.json', function (data) {
        var output = "";
         output += '<li data-role="list-divider" class="ui-center"><div data-role="fieldcontain"><fieldset data-role="controlgroup" data-type="horizontal"><label for="select-choice-cat">Category</label><select name="select-choice-cat" id="select-choice-cat"><option>Category</option><option value="All">All</option><option value="Fashion">Fashion</option><option value="Kitchen">Kitchen</option><option value="Restaurant">Restaurant</option><option value="Electronics">Electronics</option><option value="Shoes & Bags">Shoes & Bags</option><option value="Cosmetics">Cosmetics</option></select><label for="select-choice-card">Card</label><select name="select-choice-card" id="select-choice-card"><option>Card</option><option value="CMB">CMB</option><option value="POSB">POSB</option><option value="DBS">DBS</option><option value="UOB">UOB</option><option value="CITI Bank">CITI Bank</option><option value="Safra">Safra</option><option value="Passion">Passion</option><option value="AMEX">AMEX</option></select><label for="select-choice-loc">LOC</label><select name="select-choice-loc" id="select-choice-loc"><option value="1">All</option><option value="2">Loc 1</option><option value="3">Loc 2</option><option value="4">Loc 3</option></select></fieldset></div></li>';*/

    //console.info("Fil: " + fil);
    /*$.getJSON('todays.json', function (data) {
        var output = "";
        if (fil) {
            if (fil == "all") {
                for (var i in data.todays) {
                    var dist = data.todays[i].dist;
                    //convert km to m and concat unit after values
                    if (dist < 1) {
                        dist = (dist * 1000);
                        dist = dist + "m";
                    } else {
                        dist = dist + "km";
                    }
                    output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                }
            } else {
                for (var i in data.todays) {
                    if (data.todays[i].category == fil) {
                        var dist = data.todays[i].dist;
                        //convert km to m and concat unit after values
                        if (dist < 1) {
                            dist = (dist * 1000);
                            dist = dist + "m";
                        } else {
                            dist = dist + "km";
                        }
                        output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                    }
                }
            }
        } else {
            for (var i in data.todays) {
                var dist = data.todays[i].dist;
                //convert km to m and concat unit after values
                if (dist < 1) {
                    dist = (dist * 1000);
                    dist = dist + "m";
                } else {
                    dist = dist + "km";
                }
                output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
            }
        }
        $("#ListDiv").html('<ul id="List" data-role="listview"></ul>');
        $('#List').append(output);
        $('#List').listview(); //<-- here

    });*/

}



function filter_card(fil) {
    //console.info("filter_card: " + fil);    

    /*
            var output = "";

        if (fil) {
            if (fil == "all") {
                for (var i in todayspecial_data) {
                    var img = imageUrl+todayspecial_data[i].default_image;
                    var dist = (Math.random() * 5) + 1;
                    dist = dist.toFixed(2)
                    //convert km to m and concat unit after values
                    if (dist < 1) {
                        dist = (dist * 1000);
                        dist = dist + "m";
                    } else {
                        dist = dist + "km";
                    }
                    
                     output +="<div class='premium-item-box'><div class='premium-item-nice'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img src='" + img + "' /></a></div><div class='canter-taxt'><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1'><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice'>" + dist + "</div></div>";
                    
//                    output += "<li><a href='" + todayspecial_data[i].id + "'> <img src='" + img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + todayspecial_data[i].title + "</h2> <p>" + todayspecial_data[i].location + "</p> <p>" + todayspecial_data[i].card + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + todayspecial_data[i].category_id + "</h3><p style='text-align: -webkit-right;'>" + todayspecial_data[i].offer_text + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                }
            } else {
                for (var i in todayspecial_data) {

                    if (fil == todayspecial_data[i]card) {
                        var img = imageUrl+todayspecial_data[i].default_image;
                        var dist = (Math.random() * 5) + 1;
                        dist = dist.toFixed(2)
                        //convert km to m and concat unit after values
                        if (dist < 1) {
                            dist = (dist * 1000);
                            dist = dist + "m";
                        } else {
                            dist = dist + "km";
                        }

                         output +="<div class='premium-item-box'><div class='premium-item-nice'><div class='premium-img-nice'><a href='#'  onclick='display_details(todayspecial_data[" + i + "])'><img src='" + img + "' /></a></div><div class='canter-taxt'><div class='premium-item-text-nice'>" + todayspecial_data[i].title + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].location + "</div><div class='premium-item-te-nice'>" + todayspecial_data[i].card + "</div></div><div class='canter-taxt-1'><div class='premium-item-te-nice-1'>" + todayspecial_data[i].category_id + "</div><div class='premium-item-te-nice-1'>" + todayspecial_data[i].offer_text + "</div></div></div><div class='right-text-nice'>" + dist + "</div></div>";
                        
//                        output += "<li><a href='" + todayspecial_data[i].id + "'> <img src='" + img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + todayspecial_data[i].title + "</h2> <p>" + todayspecial_data[i].location + "</p> <p>" + todayspecial_data[i].card + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + todayspecial_data[i].category_id + "</h3><p style='text-align: -webkit-right;'>" + todayspecial_data[i].offer_text + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                    }
                }
            }
        }
//        $("#ListDiv").html('<ul id="List" data-role="listview"></ul>');
        $('#div_List').append(output);
//        $('#List').listview(); //<-- here
    
*/



    $.getJSON('todays.json', function (data) {
        var output = "";

        if (fil) {
            if (fil == "all") {
                for (var i in data.todays) {
                    var dist = data.todays[i].dist;
                    //convert km to m and concat unit after values
                    if (dist < 1) {
                        dist = (dist * 1000);
                        dist = dist + "m";
                    } else {
                        dist = dist + "km";
                    }
                    output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                }
            } else {
                for (var i in data.todays) {

                    var c_list = data.todays[i].cards.split("/");

                    if (_.contains(c_list, fil)) {
                        var dist = data.todays[i].dist;
                        //convert km to m and concat unit after values
                        if (dist < 1) {
                            dist = (dist * 1000);
                            dist = dist + "m";
                        } else {
                            dist = dist + "km";
                        }

                        output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                    }
                }
            }
        }
        $("#ListDiv").html('<ul id="List" data-role="listview"></ul>');
        $('#List').append(output);
        $('#List').listview(); //<-- here

    });
}

function filter_range(fil) {
    $.getJSON('todays.json', function (data) {
        var output = "";


        if (fil) {
            if (fil == 0) {
                for (var i in data.todays) {
                    var dist = data.todays[i].dist;
                    //convert km to m and concat unit after values
                    if (dist < 1) {
                        dist = (dist * 1000);
                        dist = dist + "m";
                    } else {
                        dist = dist + "km";
                    }
                    output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";
                }
            } else {

                for (var i in data.todays) {
                    var dist = data.todays[i].dist;
                    //console.info("Dist: "+dist);
                    if (dist <= fil) {
                        //convert km to m and concat unit after values
                        if (dist < 1) {
                            dist = (dist * 1000);
                            dist = dist + "m";
                        } else {
                            dist = dist + "km";
                        }
                        //console.info("filter range call with value of: " + fil + "and dist: " + dist);
                        output += "<li><a href='" + data.todays[i].href + "'> <img src='" + data.todays[i].img + "'> <div class='ui-grid-a'><div class='ui-block-a'><div class='ui-bar' style='height:60px'> <h2>" + data.todays[i].name + "</h2> <p>" + data.todays[i].place + "</p> <p>" + data.todays[i].cards + "</p></div></div><div class='ui-block-b'><div class='ui-bar' style='height:60px'><h3 style='text-align: -webkit-right;'>" + data.todays[i].category + "</h3><p style='text-align: -webkit-right;'>" + data.todays[i].offer + "</p><p style='text-align: -webkit-right;'>" + dist + "</p></div></div></div></a></li>";

                    }
                }

            }


        }

        $("#ListDiv").html('<ul id="List" data-role="listview"></ul>');
        $('#List').append(output);
        $('#List').listview(); //<-- here

    });

}

function fashion_gallery() {
    var output = "";

    $.getJSON('fashion_gallery.json', function (data) {


        output = '<tr><td>Nearest:</td><td style="color:#f48115;">' + data.nearest + '</td></tr><tr><td>Other:</td><td style="color:#f48115;">' + data.other + '</td></tr><tr><td>Hours:</td><td style="color:#f48115;">' + data.hours + '</td></tr><tr><td>Offer:</td><td style="color:#f48115;">' + data.offer + '</td></tr><tr><td>Valid:</td><td style="color:#f48115;">' + data.valid + '</td></tr>';

    });

}

function login_cancle() {
    // $('#select-login').prop('selectedIndex',0);

    /*var myselect = $("select#select-login");
    myselect[0].selectedIndex = 0;
    myselect.selectmenu("refresh");*/
    /*$('#select-login').selectedIndex(0).select("refresh");*/
    $('#login_email').val("");
    $('#password').val("");

}

function login() {
    $.mobile.loading("show", {
        text: "Login...",
        textVisible: "true",
        theme: "b",
    });

    var login_as = $('#select-login').val(),
        login_email = $('#login_email').val(),
        login_pass = $('#password').val();

    var data = {
        'email': login_email,
        'password': login_pass,
        'company_type': login_as
    };

    //console.info(data);
    var url = serviceUrl + 'login.php';


    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function (result) {
            //console.info(result.status);
            $.mobile.loading("hide");
            var stat;
            if (result.status == 0) {
                stat = "Invalid username or password";
            } else if (result.status == 1) {
                stat = "Login successfully";
            } else if (result.status == 2) {
                stat = "Please provide complete information";
            }

            $.mobile.loading("show", {
                text: stat,
                textVisible: "true",
                textonly: "textonly",
                theme: "b",
            });

            setTimeout(function () {
                $.mobile.loading("hide");
                if (result.status == 1) {
                    $.mobile.changePage("#mainpage");
                }
            }, 3000);

        },
        failure: function (err) {
            //console.error("reg error: " + err);
            $.mobile.loading("hide");
        },
        dataType: "json"
    });

}


function reset_reg() {
    $('#email').val("");
    $('#name').val("");
    $('#cpass').val("");
    $('#companyreg').val("");
    $('#company-type').val("Mall");
    $('#company-type').selectmenu("refresh");
    $('#country').val("india");
    $('#country').selectmenu("refresh");
}

/*Function for register user*/
function register() {

    $.mobile.loading("show", {
        text: "Register...",
        textVisible: "true",
        theme: "b",
    });

    var cemail = $('#email').val(),
        cname = $('#name').val(),
        cpass = $('#cpass').val(),
        creg = $('#companyreg').val(),
        ctype = $('#company-type').val(),
        country = $('#country').val();

    var data = {
        'email': cemail,
        'password': cpass,
        'company_name': cname,
        'comp_reg_number': creg,
        'company_type': ctype,
        'country_comp_reg_in': country
    };

    var url = serviceUrl + 'register.php';

    /*console.log(data);*/

    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function (result) {
            //console.info(result);
            $.mobile.loading("hide");

            var stat;
            if (result.status == 0) {
                stat = "Some other problem to register user";
            }
            if (result.status == 1) {
                stat = "User Register Successfully";
            }
            if (result.status == 3) {
                stat = "Please provide complete information";
            }

            $.mobile.loading("show", {
                text: stat,
                textVisible: "true",
                textonly: "textonly",
                theme: "b",
            });

            setTimeout(function () {
                $.mobile.loading("hide");
                if (result.status == 1) {
                    localStorage.setItem('user_id', result.user_id);
                    $.mobile.changePage("#mainpage");
                }
            }, 3000);


        },
        failure: function (err) {
            //console.error("reg error: " + err);
        },
        dataType: "json"
    });

}

function reset_search() {
    $('#keywords').val("");
    $('#search_category').val("all");
    $('#search_category').selectmenu("refresh");
    $('#search_card').val("all");
    $('#search_card').selectmenu("refresh");
    $('#search_location').val("all");
    $('#search_location').selectmenu("refresh");
}

/*Function for display search result*/
function display_search(s_data) {
    var output = "";

    var res_data = _.intersection(s_data.search_keyword_cat, s_data.search_card);

    for (var i in s_data.search_keyword_cat) {
        var img;
        if (s_data.search_keyword_cat[i].default_image) {
            img = imageUrl + s_data.search_keyword_cat[i].default_image;
        } else {
            img = "img/imageNotFound.jpg";
        }
        var dist = (Math.random() * 5) + 1;
        dist = dist.toFixed(2)
        //convert km to m and concat unit after values
        if (dist < 1) {
            dist = (dist * 1000);
            dist = dist + "m";
        } else {
            dist = dist + "km";
        }

        output += "<div class='premium-item-box'><div class='premium-item-nice' data-option='" + s_data.search_keyword_cat[i].id + "'><div class='premium-img-nice'><a href='#'  onclick='display_details(search_result_data.search_keyword_cat[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + s_data.search_keyword_cat[i].title + "</div><div class='premium-item-te-nice'>" + s_data.search_keyword_cat[i].location + "</div><div class='premium-item-te-nice'>" + s_data.search_keyword_cat[i].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + s_data.search_keyword_cat[i].category_id + "</div><div class='premium-item-te-nice-1'>" + s_data.search_keyword_cat[i].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";

    }

    $("#search_pg_ListDiv").html('<ul id="search_List" data-role="listview"></ul>');
    $('#search_List').append(output);
    $('#search_List').listview();


    $.mobile.changePage('#search_result_pg');
    $(".premium-item-nice").bind("taphold", tapholdHandler);


}


/*Function for search*/
function search_key() {
    $.mobile.loading("show", {
        text: "Searching...",
        textVisible: "true",
        theme: "b",
    });

    var s_keyword = $('#keywords').val(),
        s_category = $('#search_category').val(),
        s_card = $('#search_card').val();
    /*s_range = $('#password').val();*/

    if (s_category == "all") {
        s_category = "*";
    }

    if (s_card == "all") {
        s_card = "*";
    }

    var data = {
        'keyword': s_keyword,
        'category': s_category,
        'card': s_card,
        'location': "50"
    };

    //console.info(data);
    var url = serviceUrl + 'search.php';


    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function (result) {
            //console.info(result.status);
            $.mobile.loading("hide");
            //console.info(result);
            if (Object.keys(result.search_card).length || Object.keys(result.search_keyword_cat).length) {
                //console.warn("Ther is a resule");
                search_result_data = result;
                display_search(result);
            } else {
                $('#popup_result_notfound').popup('open');
            }
        },
        failure: function (err) {
            console.error("reg error: " + err);
        },
        dataType: "json"
    });

}

function display_details(list_data) {
    var html_out, html_img, img_src;

    if (list_data.default_image) {
        img_src = imageUrl + list_data.default_image;
    } else {
        img_src = "img/imageNotFound.jpg";
    }


    html_img = '<img width="100%" src="' + img_src + '">';

    $("#details_div_img").html(html_img);

    $("#details_title").html(list_data.title);

    html_out = '<tr><td>Card:</td><td style="color:#f48115;">' + list_data.card + '</td></tr><tr><td>Location:</td><td style="color:#f48115;" onclick="open_map_for_position(' + list_data.latitude + ',' + list_data.longitude + ',\'' + list_data.location + '\')">' + list_data.location + '</td></tr><tr><td>Hours:</td><td style="color:#f48115;">' + list_data.hours + '</td></tr><tr><td>Offer:</td><td style="color:#f48115;">' + list_data.offer_text + '</td></tr><tr><td>Discount:</td><td style="color:#f48115;">' + list_data.discount_amt + '</td></tr><tr><td>Description:</td><td style="color:#f48115;">' + list_data.description + '</td></tr><tr><td>Valid:</td><td style="color:#f48115;">' + list_data.start_date + ' to ' + list_data.end_date + '</td></tr><tr><td>Terms & condition:</td><td style="color:#f48115;">' + list_data.terms_condition + '</td></tr>';

    $('#details_tbody').html(html_out);

    $.mobile.changePage('#details_pg');

}

function open_map_for_position(op_lat, op_lng, op_name) { //

    $('#disp_location_header').html(op_name);
    sessionStorage.disp_Lat = op_lat;
    sessionStorage.disp_Lng = op_lng;
    $.mobile.changePage('#dispmapg');

    document.getElementById('disp_map_iframe').contentWindow.on_page_start();
}

function add_item_to_selected_list() {
    //console.log($('#select_shop_list').val());

    //alert($('#select_shop_list').val());
    if ($('#select_shop_list').val() == null) {
        // alert("in if");
        $.mobile.loading("show", {
            text: "Please Create New list First.",
            textVisible: "true",
            textonly: "textonly",
            theme: "b",
        });
        setTimeout(function () {
            $.mobile.loading("hide");
        }, 800);
    } else {
        // alert("in else");
        var res = add_to_shopping_list($('#select_shop_list').val(), sessionStorage.tapohld_id);
        shopping_list = JSON.parse(localStorage.getItem("shopping_list"));

        if (res == 1) {
            $.mobile.loading("show", {
                text: "Added To List",
                textVisible: "true",
                textonly: "textonly",
                theme: "b",
            });
        } else if (res == 0) {
            $.mobile.loading("show", {
                text: "Already in List",
                textVisible: "true",
                textonly: "textonly",
                theme: "b",
            });
        } else if (res == 2) {
            $.mobile.loading("show", {
                text: "ID is not specified",
                textVisible: "true",
                textonly: "textonly",
                theme: "b",
            });
        }
        setTimeout(function () {
            $.mobile.loading("hide");
            window.history.back();
        }, 800);
    }
    //update_data();
}


//Function to add product ot shopping_list
function add_to_shopping_list(shop_lst, ids) {
    var res = 0;
    var list_name = _.keys(shopping_list);

    if (ids == "null") {
        res = 2;
        return res
    }

    if (_.contains(list_name, shop_lst)) {

        //console.log("if called in add to shop");
        var tmp_ids = shopping_list[shop_lst];
        if (tmp_ids == " ") {
            //console.log("tmp_ids: "+tmp_ids);
            tmp_ids[tmp_ids.length] = ids;
            tmp_ids.splice(0, 1);
            res = 1;
        } else {

            if (_.contains(tmp_ids, ids)) {
                res = 0;
            } else {
                //console.log("tmp_ids: "+tmp_ids);
                tmp_ids[tmp_ids.length] = ids;
                res = 1;
            }
        }
        //console.info("after tmp_ids: "+tmp_ids);
        shopping_list[shop_lst] = tmp_ids;
    } else {
        //console.log("else called in add to shop");
        var tmp = {};
        tmp[shop_lst] = ids.split(",");
        //console.log(tmp);
        if (shopping_list) {
            //console.log("in if shopping_list");
            _(shopping_list).extend(tmp);
            res = 1;
        } else {
            //console.log("in else shopping_list");
            //console.warn(shopping_list); 
            //shopping_list.push(tmp);
            shopping_list = {};
            _(shopping_list).extend(tmp);
            //console.log(shopping_list);
            res = 1;
        }
    }

    //console.warn(shopping_list);
    //var tmp_str = JSON.stringify(shopping_list);

    localStorage.setItem('shopping_list', JSON.stringify(shopping_list));
    shopping_list = JSON.parse(localStorage.getItem("shopping_list"));
    return res

}

//Function to add new shopping_list
function add_new_shp_list() {
    //console.log("add new shp list called");
    var shop_lst_name = $("#list_name").val();
    // alert(shop_lst_name);
    if (shop_lst_name == "") {
        $.mobile.loading("show", {
            text: "Please Enter list Name.",
            textVisible: "true",
            textonly: "textonly",
            theme: "b",
        });
        $("#list_name").focus();
    } else {
        add_to_shopping_list(shop_lst_name, " ");
        $("#list_name").val("");
        shopping_list = JSON.parse(localStorage.getItem("shopping_list"));
        $.mobile.changePage('#mainpage');
    }
    //window.history.back();
    //update_data();
    //console.warn(shopping_list);
    /*_(shopping_list).extend(tmp);
    console.info(shopping_list);
    localStorage.setItem('shopping_list', JSON.stringify(shopping_list));*/
}

//Event for taphold and add to shopping_list
$(function () {
    $(".premium-item-nice").bind("taphold", tapholdHandler);
});

function tapholdHandler(event) {
    //console.info("taphold fired...");
    var tmp_id = $(this).data('option');
    var list_name = _.keys(shopping_list);
    var total_shop_list =shopping_list && Object.keys(shopping_list).length || 0,
        html_out = '';

    sessionStorage.tapohld_id = tmp_id;
    for (var i = 0; i < total_shop_list; i++) {
        html_out += '<option value="' + list_name[i] + '">' + list_name[i] + '</option>';
    }

    $(select_shop_list).html(html_out).select("refresh");


    $.mobile.changePage('#addtolist');

}
//End taphold


//Function to add shopping_list data in shopping list page
function show_shp_list() {



    var html_out = "";
    shopping_list = JSON.parse(localStorage.getItem("shopping_list"));

    if (shopping_list) {

        setTimeout(function () {
            $.mobile.loading("show", {
                text: "Loading...",
                textVisible: "true",
                theme: "b",
            });
        }, 1);

        var list_name = _.keys(shopping_list);
        var total_shop_list =shopping_list && Object.keys(shopping_list).length || 0;

        if (total_shop_list < 1) {
            //alert("NO List");
            setTimeout(function () {
                $.mobile.loading('hide');
                $("#toggle-view-list").html("");
            }, 300);
            return
        }

        var data = {
            'keyword': '',
            'category': '*',
            'card': '*',
            'location': ''
        };

        //console.info(data);
        var url = serviceUrl + 'search.php';


        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (result) {
                //console.info(result.status);
                $.mobile.loading("hide");
                //console.info(result);
                if (Object.keys(result.search_card).length || Object.keys(result.search_keyword_cat).length) {


                    for (var i = 0; i < total_shop_list; i++) {
                        html_out += '<li class="shp_list_li"><h3 class="shp_list_h3">' + list_name[i] + '</h3><div class="shp_list_p"> '; //<span class="shp_list_span"><img src="img/arrow-se-1.png"></span>

                        var tmp_list = shopping_list[list_name[i]];
                        //console.warn("tmp_list");
                        for (var j = 0; j < tmp_list.length; j++) {
                            //console.log(tmp_list[j]);


                            for (var c in result.search_keyword_cat) {
                                //console.log("cat id: " + result.search_keyword_cat[c].category_id);
                                var cid = result.search_keyword_cat[c].id;

                                if (tmp_list[j] == cid) {
                                    //html_out += '<li>';
                                    var img;
                                    if (result.search_keyword_cat[c].default_image) {
                                        img = imageUrl + result.search_keyword_cat[c].default_image;
                                    } else {
                                        img = "img/imageNotFound.jpg";
                                    }
                                    var dist = cal_dist(result.search_keyword_cat[c].latitude, result.search_keyword_cat[c].longitude);

                                    dist = dist.toFixed(2);
                                    //convert km to m and concat unit after values
                                    if (dist < 1) {
                                        dist = (dist * 1000);
                                        dist = dist + "m";
                                    } else {
                                        dist = dist + "km";
                                    }

                                    html_out += "<div class='premium-item-box'><div class='premium-item-nice'><div class='premium-img-nice'><a href='#'  onclick='display_details(result.search_keyword_cat[" + i + "])'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + result.search_keyword_cat[c].title + "</div><div class='premium-item-te-nice'>" + result.search_keyword_cat[c].location + "</div><div class='premium-item-te-nice'>" + result.search_keyword_cat[c].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + result.search_keyword_cat[c].category_id + "</div><div class='premium-item-te-nice-1'>" + result.search_keyword_cat[c].offer_text + "</div></div></div><div class='right-text-nice  '>" + dist + "</div></div>";

                                    //html_out += '</li>';
                                }

                            }


                        }

                        //End li and div ul tag
                        html_out += '</div></li>';
                    }

                    //console.info(html_out);

                    $("#toggle-view-list").html(html_out);

                    $('#toggle-view-list li').click(function () {

                        //console.log("On click fire");
                        //console.log(this);
                        //        var text = $(this).children('div').children('p');
                        var text = $(this).children('div');

                        if (text.is(':hidden')) {
                            text.slideDown('200');
                            //$(this).children('span').html('<img src="img/arrow-se-2.png" />');
                        } else {
                            text.slideUp('200');
                            //$(this).children('span').html('<img src="img/arrow-se-1.png" />');
                        }

                    });

                    //$.mobile.loading("hide");
                    setTimeout(function () {
                        $.mobile.loading('hide');
                    }, 300);

                } else {
                    //$('#popup_result_notfound').popup('open');
                    setTimeout(function () {
                        $.mobile.loading('hide');
                    }, 300);
                    //console.warn("in else shopping list display ");
                    //$.mobile.loading("hide");
                }
            },
            failure: function (err) {
                console.error("reg error: " + err);
                //$.mobile.loading("hide");
                setTimeout(function () {
                    $.mobile.loading('hide');
                }, 300);
            },
            dataType: "json"
        });
    }

}


function show_list_in_edit() {
    $.mobile.changePage('#edit_shopping_list');
    var list_name = _.keys(shopping_list);
    var total_shop_list =shopping_list && Object.keys(shopping_list).length || 0,
        html_out = '';

    for (var i = 0; i < total_shop_list; i++) {
        //        html_out += '<li data-role="none"><h3>' + list_name[i] + '</h3><a href="#" class="delete">Delete</a></li>' ;

        html_out += "<li><a href='#' onclick='edit_list(\"" + list_name[i] + "\")'>" + list_name[i] + " </a><a href='#' onclick='delete_list(this,\"" + list_name[i] + "\")' data-icon='delete'>Delete</a></li>";
    }

    $("#edit_shp_ul").html(html_out).listview("refresh");

}

function delete_list(btn, list_key) {

    //console.info(btn);
    delete shopping_list[list_key];
    var listitem = $(btn).parent();
    //console.warn($(btn).parent());
    listitem.remove();
    //$("#li1").remove();
    localStorage.setItem('shopping_list', JSON.stringify(shopping_list));
    $("#edit_shp_ul").listview("refresh");
}

function delete_list_item(btn, item_key, list_key) {

    //console.info(btn);


    var tmp_ids = shopping_list[list_key];

    //console.log("tmp_ids: "+tmp_ids);
    var index = tmp_ids.indexOf(item_key);
    if (index > -1) {
        tmp_ids.splice(index, 1);
    }


    shopping_list[list_key] = tmp_ids;



    var listitem = $(btn).parent();
    //console.warn(listitem.parent());
    listitem.parent().remove();
    //$("#li1").remove();
    localStorage.setItem('shopping_list', JSON.stringify(shopping_list));
    //$("#edit_shp_ul").listview("refresh");
}

function edit_list(lst_edit) {
    //console.info(lst_edit);
    $.mobile.changePage('#edit_shopping_list_items');

    $("#edit_shp_list_titel").html(lst_edit);
    setTimeout(function () {
        $.mobile.loading('show');
    }, 1);

    var html_out = "";
    shopping_list = JSON.parse(localStorage.getItem("shopping_list"));

    if (shopping_list[lst_edit]) {


        var data = {
            'keyword': '',
            'category': '*',
            'card': '*',
            'location': ''
        };

        //console.info(data);
        var url = serviceUrl + 'search.php';


        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (result) {
                //console.info(result.status);
                //$.mobile.loading("hide");
                //console.info(result);
                if (Object.keys(result.search_card).length || Object.keys(result.search_keyword_cat).length) {

                    var tmp_list = shopping_list[lst_edit];
                    //console.warn("tmp_list");
                    for (var j = 0; j < tmp_list.length; j++) {
                        //console.log(tmp_list[j]);


                        for (var c in result.search_keyword_cat) {
                            //console.log("cat id: " + result.search_keyword_cat[c].category_id);
                            var cid = result.search_keyword_cat[c].id;

                            if (tmp_list[j] == cid) {
                                //html_out += '<li>';
                                var img;
                                if (result.search_keyword_cat[c].default_image) {
                                    img = imageUrl + result.search_keyword_cat[c].default_image;
                                } else {
                                    img = "img/imageNotFound.jpg";
                                }
                                var dist = cal_dist(result.search_keyword_cat[c].latitude, result.search_keyword_cat[c].longitude);

                                dist = dist.toFixed(2);
                                //convert km to m and concat unit after values
                                if (dist < 1) {
                                    dist = (dist * 1000);
                                    dist = dist + "m";
                                } else {
                                    dist = dist + "km";
                                }

                                html_out += "<div class='premium-item-box'><div class='premium-item-nice'><div class='premium-img-nice'><a href='#'><img width='91px' src='" + img + "' /></a></div><div class='canter-taxt  '><div class='premium-item-text-nice'>" + result.search_keyword_cat[c].title + "</div><div class='premium-item-te-nice'>" + result.search_keyword_cat[c].location + "</div><div class='premium-item-te-nice'>" + result.search_keyword_cat[c].card + "</div></div><div class='canter-taxt-1  '><div class='premium-item-te-nice-1'>" + result.search_keyword_cat[c].category_id + "</div><div class='premium-item-te-nice-1'>" + result.search_keyword_cat[c].offer_text + "</div></div></div><div class='right-text-nice  '><a href='#' onclick='delete_list_item(this,\"" + tmp_list[j] + "\",\"" + lst_edit + "\")' data-icon='delete' class='ui-btn ui-icon-delete ui-btn-icon-notext ui-corner-all'>Delete</a></div></div>";

                                //html_out += '</li>';
                            }

                        }


                    }



                    //console.info(html_out);

                    $("#view_list_items").html(html_out);

                    //$.mobile.loading("hide");
                    setTimeout(function () {
                        $.mobile.loading('hide');
                    }, 300);

                } else {
                    //$('#popup_result_notfound').popup('open');
                    setTimeout(function () {
                        $.mobile.loading('hide');
                    }, 300);
                    //console.warn("in else shopping list display ");
                    //$.mobile.loading("hide");
                }
            },
            failure: function (err) {
                console.error("reg error: " + err);
                //$.mobile.loading("hide");
                setTimeout(function () {
                    $.mobile.loading('hide');
                }, 300);
            },
            dataType: "json"
        });
    }



}


function iframesize() {
    var height = (window.innerHeight > 0) ? window.innerHeight : screen.Height;

    //console.info("Height: " + height);
    //console.info("Mainvavi Height: " + $('.nav').height());
    var ifheight = parseInt(height) - ($('.nav').height() + $('.content-heading').height() + 10);
    document.getElementById('disp_map_iframe').style.height = ifheight + "px";
    //console.info("if Height: " + ifheight);
    //document.getElementById('current_iframe_map').style.height=ifheight+"px";
    //document.getElementById('current_map_iframe').style.height=ifheight+"px";
}

function current_iframesize() {
    var height = (window.innerHeight > 0) ? window.innerHeight : screen.Height;

    //console.info("Height: " + height);
    //console.info("Mainvavi Height: " + $('.nav').height());
    var ifheight = parseInt(height) - ($('.nav').height() + $('.content-heading').height() + 10);
    //document.getElementById('current_iframe_map').style.height=ifheight+"px";
    document.getElementById('current_map_iframe').style.height = ifheight + "px";
    //console.info("if Height: " + ifheight);
}



/*



var list_name = _.keys(shopping_list);
    var total_shop_list =shopping_list && Object.keys(shopping_list).length || 0,
        html_out = '';

    sessionStorage.tapohld_id = tmp_id;
    for (var i = 0; i < total_shop_list; i++) {
        html_out += '<li><div>' + list_name[i] + '</div><a href="#" class="delete">Delete</a></li>' ;
    }

    $("#edit_shp_ul").html(html_out).listview("refresh");



$( ".delete" ).on( "click", function() {
            var listitem = $( this ).parent( "li.ui-li" );
            listitem.remove();
                $( "#edit_shp_ul" ).listview( "refresh" );
        });
        
        
         <ul id="edit_shp_ul" class="touch" data-role="listview" data-icon="false" data-split-icon="delete" data-split-theme="d">
            <li>
               
                <a href="#" class="delete">Delete</a>
            </li>
            <li>
                
                <a href="#" class="delete">Delete</a>
            </li>
            </ul>
        
        


*/