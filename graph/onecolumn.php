<!DOCTYPE HTML>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Highcharts Example</title>
    <style type="text/css">
	body{
    font-family::'Segoe UI',Arial,Helvetica,sans-serif;
    font-size: 14px;}
	.banner{
		width:1252px;
		height:auto;}
	#user-banner{
		font-size:18px;
		background: rgb(181,189,200); /* Old browsers */
/* IE9 SVG, needs conditional override of 'filter' to 'none' */
background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2I1YmRjOCIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjM2JSIgc3RvcC1jb2xvcj0iIzgyOGM5NSIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiMyODM0M2IiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
background: -moz-linear-gradient(top,  rgba(181,189,200,1) 0%, rgba(130,140,149,1) 36%, rgba(40,52,59,1) 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(181,189,200,1)), color-stop(36%,rgba(130,140,149,1)), color-stop(100%,rgba(40,52,59,1))); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(top,  rgba(181,189,200,1) 0%,rgba(130,140,149,1) 36%,rgba(40,52,59,1) 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(top,  rgba(181,189,200,1) 0%,rgba(130,140,149,1) 36%,rgba(40,52,59,1) 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(top,  rgba(181,189,200,1) 0%,rgba(130,140,149,1) 36%,rgba(40,52,59,1) 100%); /* IE10+ */
background: linear-gradient(to bottom,  rgba(181,189,200,1) 0%,rgba(130,140,149,1) 36%,rgba(40,52,59,1) 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#b5bdc8', endColorstr='#28343b',GradientType=0 ); /* IE6-8 */

		color:#FFFFFF;
		width:52.6%;
		visibility:hidden;
		float:left;
		border-top-right-radius:5px;
		border-top-left-radius:5px;		
	}
	#news-banner{
		font-size:18px;
		background-color:#733171;
		color:#FFFFFF;
		width:318px;
		visibility:hidden;		
		float:right;
	}
	iframe {
    width: 800px;
    height: 520px;
    border: none;
    position:absolute;
	top:100px;
	left:100px;
	z-index:1;
}
.line{border-bottom:solid #09C 1px;}
.info{float:left; margin-left:10px;}
.tweets{height:75px; width:100%; border-bottom:solid  #39F 1px;
background: rgb(240,249,255); /* Old browsers */
/* IE9 SVG, needs conditional override of 'filter' to 'none' */
background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2YwZjlmZiIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjQ3JSIgc3RvcC1jb2xvcj0iI2NiZWJmZiIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiNhMWRiZmYiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
background: -moz-linear-gradient(top,  rgba(240,249,255,1) 0%, rgba(203,235,255,1) 47%, rgba(161,219,255,1) 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(240,249,255,1)), color-stop(47%,rgba(203,235,255,1)), color-stop(100%,rgba(161,219,255,1))); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(top,  rgba(240,249,255,1) 0%,rgba(203,235,255,1) 47%,rgba(161,219,255,1) 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(top,  rgba(240,249,255,1) 0%,rgba(203,235,255,1) 47%,rgba(161,219,255,1) 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(top,  rgba(240,249,255,1) 0%,rgba(203,235,255,1) 47%,rgba(161,219,255,1) 100%); /* IE10+ */
background: linear-gradient(to bottom,  rgba(240,249,255,1) 0%,rgba(203,235,255,1) 47%,rgba(161,219,255,1) 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f0f9ff', endColorstr='#a1dbff',GradientType=0 ); /* IE6-8 */

}
.tweetss{height:75px; width:100%; border-bottom:solid  #39F 1px;
background: rgb(238,238,238); /* Old browsers */
/* IE9 SVG, needs conditional override of 'filter' to 'none' */
background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2VlZWVlZSIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiNlZWVlZWUiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
background: -moz-linear-gradient(top,  rgba(238,238,238,1) 0%, rgba(238,238,238,1) 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(238,238,238,1)), color-stop(100%,rgba(238,238,238,1))); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(top,  rgba(238,238,238,1) 0%,rgba(238,238,238,1) 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(top,  rgba(238,238,238,1) 0%,rgba(238,238,238,1) 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(top,  rgba(238,238,238,1) 0%,rgba(238,238,238,1) 100%); /* IE10+ */
background: linear-gradient(to bottom,  rgba(238,238,238,1) 0%,rgba(238,238,238,1) 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#eeeeee', endColorstr='#eeeeee',GradientType=0 ); /* IE6-8 */

}
.clear{clear:both;}
.img{width:48px; border-color:#0CC; float:left;}
.text{width:90%; height:44px; border-color:#0F9; float:right; padding-left:5px;}
.user{font-weight:700; cursor:pointer;}
</style>
    <script type="text/javascript" src="jquery-1.7.2.js"></script>
    <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDlmZn_jhR4UK9ydbiKf4Mtn9GDuZbLO5w&sensor=true">        
    </script>
    <script type="text/javascript" src="JSPlacemaker.js"></script>
    <script type="text/javascript">
/*	$(document).ready(function(){
	$('#user-tweets').on('mouseenter', "a", function() {
    $(this).append($("<iframe src='"+this.href+"'></iframe>"));
}).on('mouseleave', 'a', function () {
    $(this).find("iframe:last").remove();
});
});
*/
function search(value){
	if (value=="reuters"){value2 = value.slice(0,1).toUpperCase() + value.slice(1);
	news_tweets(value2);
	}
	else{
	news_tweets(value);
	}
}
            function news_tweets(x) {
            news_array = [];
			user_ids_2 = [];
            user_news_array = [];
            var ids_2;
				$("#news-tweets").html("");
                $.getJSON("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=false&screen_name="+x+"&count=10&callback=?",

                function (data) { //console.log(data);
                    var len = data.length;
                    for (var i = 0; i < len; ++i) {
                        var user = data[i].user.name;
                        var date = data[i].created_at;
                        var profile_img = data[i].user.profile_image_url;
                        var text = data[i].text;
                        var url = (data[i].entities.urls.length > 0 ? data[i].entities.urls[0].url : '');
                        create_news_array(user, date, profile_img, text,url);
                    }
                    function create_news_array(a, b, c, d, e) {
                        news_array.push({
                            user: a,
                            date: b,
                            profile_img: c,
                            text: d,
							url:e
                        });
                    }
                    deploy_user_tweets(news_array,x);
                });
            function deploy_user_tweets(news_array,x){
			
				$("#user-tweets").html("");
                $.getJSON("http://search.twitter.com/search.json?q=%23"+x+"&rpp=20&include_entities=true&result_type=mixed&callback=?",

                function (data) {
                    $.each(data.results, function (i, item) {
                        var user = item.from_user;
                        var user_id = item.from_user_id;
                        var date = item.created_at;
                        var profile_img = item.profile_image_url;
                        var text = item.text;
                        var url = (item.entities.urls.length > 0 ? item.entities.urls[0].url : '');
                        create_news_array(user, user_id, date, profile_img, text, url);
                    });

                    function create_news_array(a, b, c, d, e, f) {
                        user_news_array.push({
                            user: a,
                            user_id: b,
                            date: c,
                            profile_img: d,
                            text: e,
                            url: f
                        });
                    }
                    
					
                    for (var i in user_news_array) {
                        user_ids_2.push(user_news_array[i].user_id);
                        //console.log(news_array[i].user_id);
                    }
                    ids_2 = user_ids_2.join();

                    //console.log(ids_2);
                    search_location(ids_2, user_news_array,news_array);
                });
            }

            function search_location(ids_2, user_news_array,news_array) {
                $.getJSON("http://api.twitter.com/1/users/lookup.json?user_id=" + ids_2 + "&callback=?", function (data) { //first attempt to find locations
                    $.each(data, function (i, item) {
                        var location = item.location;

                        user_news_array[i].location = location; //locations are added but some are empty

                    });
                    var userLocationMap = {};
                    for (var i = 0; i < user_news_array.length; i++) {
                        if (user_news_array[i].location) {
                            userLocationMap[user_news_array[i].user] = user_news_array[i].location;
                        }
                    }

                    for (var i = 0; i < user_news_array.length; i++) {
                        if (!user_news_array[i].location) {
                            user_news_array[i].location = userLocationMap[user_news_array[i].user];
                        }
                    }
                    combine_arrays(user_news_array,news_array);
                });
            }
			function combine_arrays(user_news_array,news_array){
			full_array=user_news_array.concat(news_array);
			console.log(full_array); geo(full_array);
			}
            function geo(full_array) {
                //console.log(user_news_array);
                for (var i in full_array) {
                    var location = full_array[i].location;
                    var user = full_array[i].user;
                    var date = full_array[i].date;
                    var profile_img = full_array[i].profile_img;
                    var text = full_array[i].text;
                    var url = full_array[i].url;
					if (location!==undefined){
                    geocode_user_tweets(user, date, profile_img, text, url, location);
					}
					if (user=="euronews"||user=="Reuters Top News" ||user=="Sky News" ||user=="The Independent"){
                		geocode_news_tweets(user, date, profile_img, text, url);}		
                }
				var mapOptions = {
                    center: new google.maps.LatLng(35.74651, - 39.46289),
                    zoom: 2,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                // added this 
                var bounds = new google.maps.LatLngBounds();
                // create the map
                var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                function geocode_user_tweets(user, date, profile_img, text, url, location) {
				var templates = [];
				templates[0] = '<div><div></div><h2 class="firstHeading"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a>'+user+'</h2><div>'+text+'</div><div><p><a href="' + url + '"target="_blank">'+url+'</a></p></div><p>Date Posted- '+date+'</p></div>';
				templates[1] = '<div class="tweets"><div class="user">'+user+'</div><span class="clear"></span><div class="img"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a></div><div class="text">'+text+'<br /><a href="' + url + '"target="_blank">'+url+'</a></div>';
				templates[2] = '<div><div></div><h2 class="firstHeading"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a>'+user+'</h2><div>'+text+'</div><div><p><a href="' + url + '"target="_blank">'+url+'</a></p></div><p>Date Posted- '+date+'</p></div>';
				templates[3] = '<div class="tweetss"><div class="user">'+user+'</div><span class="clear"></span><div class="img"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a></div><div class="text">'+text+'<br /><a href="' + url + '"target="_blank">'+url+'</a></div>';
				templates[4] = '<div><div></div><h2 class="firstHeading"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a>'+user+'</h2><div>'+text+'</div><div><p><a href="' + url + '"target="_blank">'+url+'</a></p></div><p>Date Posted- '+date+'</p></div>';
				templates[5] = '<div class="tweetss"><div class="user">'+user+'</div><span class="clear"></span><div class="img"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a></div><div class="text">'+text+'<br /><a href="' + url + '"target="_blank">'+url+'</a></div>';
				templates[6] = '<div><div></div><h2 class="firstHeading"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a>'+user+'</h2><div>'+text+'</div><div><p><a href="' + url + '"target="_blank">'+url+'</a></p></div><p>Date Posted- '+date+'</p></div>';
				templates[7] = '<div class="tweetss"><div class="user">'+user+'</div><span class="clear"></span><div class="img"><a href="'+profile_img+'" target="_blank"><img src="'+profile_img+'" width="55" height="50"/></a></div><div class="text">'+text+'<br /><a href="' + url + '"target="_blank">'+url+'</a></div>';

				var geocoder = new google.maps.Geocoder();
                    geocoder.geocode({
                        address: location
                    }, function (response, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            var x = response[0].geometry.location.lat(),
                                y = response[0].geometry.location.lng();
                            var myLatLng = new google.maps.LatLng(x, y);
                            var marker = new google.maps.Marker({
                                icon: profile_img,
                                title: user,
                                map: map,
                                position: myLatLng
                            });


						  var infowindow = new google.maps.InfoWindow({
							  content: templates[0].replace('user',user).replace('text',text).replace('url',url).replace('date',date)
						  });
						  var $tweet = $(templates[1].replace('%user',user).replace(/%profile_img/g,profile_img).replace('%text',text).replace('%url',url));
	  					  $('#user-banner').css("visibility","visible");$('#news-banner').css("visibility","visible");
						  $('#user-tweets').css("overflow","scroll").append($tweet);
						  function openInfoWindow() {
							  infowindow.open(map, marker);
						  }
						  google.maps.event.addListener(marker, 'click', openInfoWindow);
						  $tweet.find(".user").on('click', openInfoWindow);
                            bounds.extend(myLatLng);
                        } else if (status === google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
                            setTimeout(function () {
                                geocode_user_tweets(user,date,profile_img,text,url,location);
                            }, 500);
                        } else {
                            Placemaker.getPlaces(text, function (o) {
                                //console.log(user,o);
                                if ($.isArray(o.match)) {
                                    if (o.match[0].place.name == "Europe" || o.match[0].place.name == "United States") {
                                        var latitude = o.match[1].place.centroid.latitude;
                                        var longitude = o.match[1].place.centroid.longitude;
                                        var myLatLng = new google.maps.LatLng(latitude, longitude);
                                        var marker = new google.maps.Marker({
                                            icon: profile_img,
                                            title: user,
                                            map: map,
                                            position: myLatLng
                                        });


						  var infowindow = new google.maps.InfoWindow({
							  content: templates[2].replace('user',user).replace('text',text).replace('url',url).replace('date',date)
						  });
						  var $tweet = $(templates[3].replace('%user',user).replace(/%profile_img/g,profile_img).replace('%text',text));
  	  					  $('#user-banner').css("visibility","visible");$('#news-banner').css("visibility","visible");
						  $('#user-tweets').css("overflow","scroll").append($tweet);
						  function openInfoWindow() {
							  infowindow.open(map, marker);
						  }
						  google.maps.event.addListener(marker, 'click', openInfoWindow);
						  $tweet.find(".user").on('click', openInfoWindow);
                                        bounds.extend(myLatLng);
                                    }}
                                  else  if ($.isArray(o.match)) {
                                        if (o.match[0].place.name !== "Europe") {
                                            var latitude = o.match[0].place.centroid.latitude;
                                            var longitude = o.match[0].place.centroid.longitude;
                                            var myLatLng = new google.maps.LatLng(latitude, longitude);
                                            var marker = new google.maps.Marker({
                                                icon: profile_img,
                                                title: user,
                                                map: map,
                                                position: myLatLng
                                            });


						  var infowindow = new google.maps.InfoWindow({
							  content: templates[4].replace('user',user).replace('text',text).replace('url',url).replace('date',date)
						  });
						  var $tweet = $(templates[5].replace('%user',user).replace(/%profile_img/g,profile_img).replace('%text',text));
  	  					  $('#user-banner').css("visibility","visible");$('#news-banner').css("visibility","visible");
						  $('#user-tweets').css("overflow","scroll").append($tweet);
						  function openInfoWindow() {
							  infowindow.open(map, marker);
						  }
						  google.maps.event.addListener(marker, 'click', openInfoWindow);
						  $tweet.find(".user").on('click', openInfoWindow);

                                            bounds.extend(myLatLng);
                                        }
                                    }
                                
                               else if (!$.isArray(o.match) && o.error !== "no locations found") {
                                    var latitude = o.match.place.centroid.latitude;
                                    var longitude = o.match.place.centroid.longitude;
                                    var myLatLng = new google.maps.LatLng(latitude, longitude);
                                    var marker = new google.maps.Marker({
                                        icon: profile_img,
                                        title: user,
                                        map: map,
                                        position: myLatLng
                                    });


						  var infowindow = new google.maps.InfoWindow({
							  content: templates[6].replace('user',user).replace('text',text).replace('url',url).replace('date',date)
						  });
						  var $tweet = $(templates[7].replace('%user',user).replace(/%profile_img/g,profile_img).replace('%text',text));
  	  					  $('#user-banner').css("visibility","visible");$('#news-banner').css("visibility","visible");
						  $('#user-tweets').css("overflow","scroll").append($tweet);
						  function openInfoWindow() {
							  infowindow.open(map, marker);
						  }
						  google.maps.event.addListener(marker, 'click', openInfoWindow);
						  $tweet.find(".user").on('click', openInfoWindow);
                                    
                                    bounds.extend(myLatLng);
                                }
                            

                        });//placemaker
                    }
                });
/*			setInterval( function() {
				 update_tweets(user_news_array,value); }, 60000 );
           function update_tweets(user_news_array,value){
			   var update_news_array=[];
		$.getJSON("http://search.twitter.com/search.json?q="+value+"&rpp=1&include_entities=true&result_type=mixed&callback=?",
                function (data) {
                    $.each(data.results, function (i, item) {
                        var user = item.from_user;
                        var user_id = item.from_user_id;
                        var date = item.created_at;
                        var profile_img = item.profile_image_url;
                        var text = item.text;
                        var url = (item.entities.urls.length > 0 ? item.entities.urls[0].url : '');
                        create_news_array(user, user_id, date, profile_img, text, url);
                    });

                    function create_news_array(a, b, c, d, e, f) {
                        update_news_array.push({
                            user: a,
                            user_id: b,
                            date: c,
                            profile_img: d,
                            text: e,
                            url: f
                        });
                    }

			} 
*/			
			}
		function geocode_news_tweets(user, date, profile_img, text, url){
                    Placemaker.getPlaces(text, function (o) {
                        console.log(o);
						  if (!$.isArray(o.match) && o.error !== "no locations found") {
							  var latitude = o.match.place.centroid.latitude;
							  var longitude = o.match.place.centroid.longitude;
                            var myLatLng = new google.maps.LatLng(latitude, longitude);
                            var marker = new google.maps.Marker({
                                icon: profile_img,
                                title: user,
                                map: map,
                                position: myLatLng
                            });

						  var infowindow = new google.maps.InfoWindow({
							  content: text
						  });
						  google.maps.event.addListener(marker, 'click', function() {
						  infowindow.open(map,marker);
							});
                            bounds.extend(myLatLng);
                        }
                       else if ($.isArray(o.match)) {

                            var latitude = o.match[0].place.centroid.latitude;
                            var longitude = o.match[0].place.centroid.longitude;
                            var myLatLng = new google.maps.LatLng(latitude, longitude);
                            var marker = new google.maps.Marker({
                                icon: profile_img,
                                title: user,
                                map: map,
                                position: myLatLng
                            });
							var infowindow = new google.maps.InfoWindow({
							  content: text
						  });
						  google.maps.event.addListener(marker, 'click', function() {
						  infowindow.open(map,marker);
							});

                            bounds.extend(myLatLng);
                      } //end of second if statement



                    });

			}
			}}//user_tweets
			
</script>
</head>
<body><div class="news" style="width:80%; text-align:center;"><div class="info"><span style="font-style: italic; color:#F00;">"Pick one of the news logos to view<br />
news generated from users and agencies"</span></div>
<img src="img/euronews.png" width="48" height="48 " title="Euronews" onclick="news_tweets('euronews')" />
<img src="img/reuters.png" width="48" height="48 " title="Reuters" onclick="search('reuters')" />
<img src="img/skynews.jpg" width="48" height="48 " title="Skynews" onclick="news_tweets('skynews')" />
<img src="img/guardian.jpg" width="48" height="48 " title="Guardian" onclick="news_tweets('guardian')" />
<img src="img/independent.jpg" width="48" height="48" title="The Independent" onclick="news_tweets('Independent')" />
</div>
<div style="clear:both;"></div>
<div class="banner"><div id="user-banner">User Tweets</div><!--<div id="news-banner">News Tweets</div>--></div><div style="clear:both;"></div>
<div id="user-tweets" style="float:left; width:52%; border:solid #FCF 1px; height:400px;"></div>
<div id="map_canvas" style="float:right; margin-left:5px;width:47%; border:solid #FCF 1px; height:400px; " ></div>

<!--<div id="news-tweets" style="float:left; width:25%; margin-left:10px; border:solid #FCF 1px; height:400px;"></div>
-->
</body>

</html>