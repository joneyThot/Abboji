/*initialize map*/

var map, marker, infowindow;


function initialize_map() {
    
    var mapOptions = {
        zoom: 3,
        center: new google.maps.LatLng(23.027381, 72.580342),
        disableDefaultUI: true
    };

   
    
    infowindow = new google.maps.InfoWindow({
        content: 'Drop point info',
    });
    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);
    
    
      // Create the search box and link it to the UI element.
  var input = /** @type {HTMLInputElement} */(
      document.getElementById('pac-input'));
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  var searchBox = new google.maps.places.SearchBox(
    /** @type {HTMLInputElement} */(input));
    
    
    
     // Create the DIV to hold the control and
  // call the HomeControl() constructor passing
  // in this DIV.
  var homeControlDiv = document.createElement('div');
  var homeControl = new HomeControl(homeControlDiv, map, "off");

  homeControlDiv.index = 1;
  map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(homeControlDiv);
    /*map_center=map.getCenter().toString();
    console.info("map_center"+map_center);*/
   // [START region_getplaces]
  // Listen for the event fired when the user selects an item from the
  // pick list. Retrieve the matching places for that item.
  google.maps.event.addListener(searchBox, 'places_changed', function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }
   /* for (var i = 0, marker; marker = markers[i]; i++) {
      marker.setMap(null);
    }*/

    // For each place, get the icon, place name, and location.
    //markers = [];
    var bounds = new google.maps.LatLngBounds();
    for (var i = 0, place; place = places[i]; i++) {
      var image = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
//      var marker = new google.maps.Marker({
//        map: map,
//        icon: image,
//        title: place.name,
//        position: place.geometry.location
//      });
//
//      markers.push(marker);

      bounds.extend(place.geometry.location);
    }

    map.fitBounds(bounds);
      map.setZoom(12);
  });
  // [END region_getplaces]

  // Bias the SearchBox results towards places that are within the bounds of the
  // current map's viewport.
  google.maps.event.addListener(map, 'bounds_changed', function() {
    var bounds = map.getBounds();
    searchBox.setBounds(bounds);
  });
    
   
    
}
google.maps.event.addDomListener(window, 'load', initialize_map);


// onSuccess Callback receives a PositionError object
var getposSuccess = function (position) {
    /*        alert('Latitude: ' + position.coords.latitude + '\n' +
            'Longitude: ' + position.coords.longitude + '\n' +
            'Altitude: ' + position.coords.altitude + '\n' +
            'Accuracy: ' + position.coords.accuracy + '\n' +
            'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
            'Heading: ' + position.coords.heading + '\n' +
            'Speed: ' + position.coords.speed + '\n' +
            'Timestamp: ' + position.timestamp + '\n');*/
    var pos = new google.maps.LatLng(position.coords.latitude,
        position.coords.longitude);
    map.setCenter(pos);
    map.setZoom(8);
   // placeMarker(pos, map);
    /*localStorage.setItem('latitude', position.coords.latitude);
        localStorage.setItem('longitude', position.coords.longitude);*/

};

// onError Callback receives a PositionError object
function posonError(error) {
    console.error('code: ' + error.code + '\n' +
        'message: ' + error.message + '\n');
}

navigator.geolocation.getCurrentPosition(getposSuccess, posonError);



/*create marker*/
function placeMarker(position, map) {
    marker = new google.maps.Marker({
        map: map,
        draggable: true,
        animation: google.maps.Animation.DROP,
        position: position
    });
    google.maps.event.addListener(marker, 'dragend', function () {
        geocodePosition(marker.getPosition());
    });

    google.maps.event.addListener(marker, 'click', function () {
        infowindow.open(map);
    });
    
    localStorage.setItem('current_pos', position);

}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
  setAllMap(null);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  //clearMarkers();
  marker.setMap(null);
}

// Sets the map on all markers in the array.
function setAllMap(map) {
  for (var i = 0; i < marker.length; i++) {
    marker[i].setMap(map);
  }
}

/*Function for fiend geo position info*/
function geocodePosition(pos) {
    geocoder = new google.maps.Geocoder();
    geocoder.geocode({
            latLng: pos
        },
        function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                infowindow.setContent(results[0].formatted_address);
                infowindow.setPosition(marker.getPosition());
                localStorage.setItem('current_pos', pos);
                localStorage.setItem('tmp_loc_name',results[0].formatted_address);
                /* $("#mapSearchInput").val(results[0].formatted_address);
                $("#mapErrorMsg").hide(100);*/
            } else {
                /*$("#mapErrorMsg").html('Cannot determine address at this location.' + status).show(100);*/
            }
        }
    );
}
/*End geocordposition Function*/



/*function set_home_location() {
    var home_LatLng = marker.getPosition();
    console.info("Home Location: " + home_LatLng);
    localStorage.setItem('home_location', home_LatLng);
}*/



$(document).on("pageinit", function () {
    $("#home_select_map iframe")
        .attr("width", 0)
        .attr("height", 0);

    $("#home_select_map iframe").contents().find("#map-canvas")
        .css({
            "width": 0,
            "height": 0
        });

    $("#home_select_map").on({
        popupbeforeposition: function () {
            var size = scale(480, 320, 0, 1),
                w = size.width,
                h = size.height;

            $("#home_select_map iframe")
                .attr("width", w)
                .attr("height", h);

            $("#home_select_map iframe").contents().find("#map-canvas")
                .css({
                    "width": w,
                    "height": h
                });
        },
        popupafterclose: function () {
            $("#home_select_map iframe")
                .attr("width", 0)
                .attr("height", 0);

            $("#home_select_map iframe").contents().find("#map-canvas")
                .css({
                    "width": 0,
                    "height": 0
                });
        }
    });
});

function scale(width, height, padding, border) {
    var scrWidth = $(window).width() - 30,
        scrHeight = $(window).height() - 30,
        ifrPadding = 2 * padding,
        ifrBorder = 2 * border,
        ifrWidth = width + ifrPadding + ifrBorder,
        ifrHeight = height + ifrPadding + ifrBorder,
        h, w;

    if (ifrWidth < scrWidth && ifrHeight < scrHeight) {
        w = ifrWidth;
        h = ifrHeight;
    } else if ((ifrWidth / scrWidth) > (ifrHeight / scrHeight)) {
        w = scrWidth;
        h = (scrWidth / ifrWidth) * ifrHeight;
    } else {
        h = scrHeight;
        w = (scrHeight / ifrHeight) * ifrWidth;
    }

    return {
        'width': w - (ifrPadding + ifrBorder),
        'height': h - (ifrPadding + ifrBorder)
    };
};



/**
 * The HomeControl adds a control to the map that
 * returns the user to the control's defined home.
 */

// Define a property to hold the Home state
HomeControl.prototype.home_ = null;

// Define setters and getters for this property
HomeControl.prototype.getHome = function() {
  return this.home_;
}

HomeControl.prototype.setHome = function(home) {
  this.home_ = home;
}

/** @constructor */
function HomeControl(controlDiv, map, home) {

  // We set up a variable for this since we're adding
  // event listeners later.
  var control = this;

  // Set the home property upon construction
  control.home_ = home;

  // Set CSS styles for the DIV containing the control
  // Setting padding to 5 px will offset the control
  // from the edge of the map
  controlDiv.style.padding = '5px';
  controlDiv.style.paddingBottom = '15px';
  controlDiv.style.left = '67px';

  // Set CSS for the control border
  var goHomeUI = document.createElement('div');
  goHomeUI.style.backgroundColor = 'white';
  goHomeUI.style.borderStyle = 'solid';
  goHomeUI.style.borderWidth = '2px';
  goHomeUI.style.cursor = 'pointer';
  goHomeUI.style.textAlign = 'center';
  goHomeUI.title = 'Click to Add Or Remove Marker';
  controlDiv.appendChild(goHomeUI);

  // Set CSS for the control interior
  var goHomeText = document.createElement('div');
  goHomeText.style.fontFamily = 'Arial,sans-serif';
  goHomeText.style.fontSize = '14px';
  goHomeText.style.paddingLeft = '4px';
  goHomeText.style.paddingRight = '4px';
  goHomeText.innerHTML = '<b>Add/Remove Marker</b>';
  goHomeUI.appendChild(goHomeText);

  // Set CSS for the setHome control border
  /*var setHomeUI = document.createElement('div');
  setHomeUI.style.backgroundColor = 'white';
  setHomeUI.style.borderStyle = 'solid';
  setHomeUI.style.borderWidth = '2px';
  setHomeUI.style.cursor = 'pointer';
  setHomeUI.style.textAlign = 'center';
  setHomeUI.title = 'Click to set Home to the current center';
  controlDiv.appendChild(setHomeUI);*/

  // Set CSS for the control interior
  /*var setHomeText = document.createElement('div');
  setHomeText.style.fontFamily = 'Arial,sans-serif';
  setHomeText.style.fontSize = '12px';
  setHomeText.style.paddingLeft = '4px';
  setHomeText.style.paddingRight = '4px';
  setHomeText.innerHTML = '<b>Set Home</b>';
  setHomeUI.appendChild(setHomeText);*/

  // Setup the click event listener for Home:
  // simply set the map to the control's current home property.
  google.maps.event.addDomListener(goHomeUI, 'click', function() {
    /*var currentHome = control.getHome();
    map.setCenter(currentHome);*/
      if(control.getHome()=="off")
      {
        placeMarker(map.getCenter(), map);
        control.setHome("on");
      }
      else
      {
        deleteMarkers();
        control.setHome("off");
      }
      
  });

  // Setup the click event listener for Set Home:
  // Set the control's home to the current Map center.
/*  google.maps.event.addDomListener(setHomeUI, 'click', function() {
    var newHome = map.getCenter();
    control.setHome(newHome);
  });*/
}








// This example adds a search box to a map, using the Google Place Autocomplete
// feature. People can enter geographical searches. The search box will return a
// pick list containing a mix of places and predicted search terms.

function initialize() {

  var markers = [];
  //var map = new google.maps.Map(document.getElementById('map-canvas'), {
  //  mapTypeId: google.maps.MapTypeId.ROADMAP
  //});

  var defaultBounds = new google.maps.LatLngBounds(
      new google.maps.LatLng(-33.8902, 151.1759),
      new google.maps.LatLng(-33.8474, 151.2631));
  map.fitBounds(defaultBounds);

  // Create the search box and link it to the UI element.
  var input = /** @type {HTMLInputElement} */(
      document.getElementById('pac-input'));
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  var searchBox = new google.maps.places.SearchBox(
    /** @type {HTMLInputElement} */(input));

  // [START region_getplaces]
  // Listen for the event fired when the user selects an item from the
  // pick list. Retrieve the matching places for that item.
  google.maps.event.addListener(searchBox, 'places_changed', function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }
    for (var i = 0, marker; marker = markers[i]; i++) {
      marker.setMap(null);
    }

    // For each place, get the icon, place name, and location.
    markers = [];
    var bounds = new google.maps.LatLngBounds();
    for (var i = 0, place; place = places[i]; i++) {
      var image = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      var marker = new google.maps.Marker({
        map: map,
        icon: image,
        title: place.name,
        position: place.geometry.location
      });

      markers.push(marker);

      bounds.extend(place.geometry.location);
    }

    map.fitBounds(bounds);
  });
  // [END region_getplaces]

  // Bias the SearchBox results towards places that are within the bounds of the
  // current map's viewport.
  google.maps.event.addListener(map, 'bounds_changed', function() {
    var bounds = map.getBounds();
    searchBox.setBounds(bounds);
  });
}

google.maps.event.addDomListener(window, 'load', initialize);


