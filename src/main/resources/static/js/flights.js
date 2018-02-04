var evtSource = new EventSource("http://localhost:8080/flights");
var map = new google.maps.Map(document.getElementById('map_canvas'), {
    zoom: 8,
    center: new google.maps.LatLng(37.77, -122.43),
    mapTypeId: google.maps.MapTypeId.ROADMAP
});

var infowindow = new google.maps.InfoWindow();

var marker;
//Used to remember markers
var markerStore = {};

// Calling main function
fetchdata();

function convertTimestamp(timestamp) {
    var d = new Date(timestamp * 1000),	// Convert the passed timestamp to milliseconds
        yyyy = d.getFullYear(),
        mm = ('0' + (d.getMonth() + 1)).slice(-2),	// Months are zero based. Add leading 0.
        dd = ('0' + d.getDate()).slice(-2),			// Add leading 0.
        hh = d.getHours(),
        h = hh,
        min = ('0' + d.getMinutes()).slice(-2),		// Add leading 0.
        ampm = 'AM',
        s= d.getSeconds(),
        time;

    if (hh > 12) {
        h = hh - 12;
        ampm = 'PM';
    } else if (hh === 12) {
        h = 12;
        ampm = 'PM';
    } else if (hh == 0) {
        h = 12;
    }

    // ie: 2013-02-18, 8:35:45 AM
    time = yyyy + '-' + mm + '-' + dd + ', ' + h + ':' + min + ':'+ s + ' ' + ampm;

    return time;
}
// main function, it gets data rom /flights endpoint, and update markers and info based on it,
function fetchdata(){
    evtSource.onmessage = function (event) {
        var state = JSON.parse(event.data);
        var data = state.states;
        data.filter(function(state){
            return state[6]  &&  state[5];
        }).forEach(function(state){
        if (markerStore.hasOwnProperty(state[0])) {
            var m = markerStore[state[0]];
            var icon = m.getIcon();
            icon.rotation = state[10] - 90;
            m.setPosition(new google.maps.LatLng(state[6] , state[5]));
            m.setIcon(icon);
        } else {
            marker = new google.maps.Marker({
                map: map,
                position: new google.maps.LatLng(state[6], state[5]),
                icon: {
                    path: "m2,106h28l24,30h72l-44,-133h35l80,132h98c21,0 21,34 0,34l-98,0 -80,134h-35l43,-133h-71l-24,30h-28l15,-47",
                    rotation: state[10]- 90,
                    scale: 0.05,
                    fillOpacity: 0.8,
                    strokeColor: 'gold',
                    fillColor: 'blue'
                }

            });

            google.maps.event.addListener(marker, 'click', (function (marker) {
                return function () {
                     infowindow.setContent('ICAO 24: '+ state[0]);
                    infowindow.open(map, marker);
                    plane_info(state);
                    aircraft(state[0]);
                }
            })(marker));
            markerStore[state[0]] = marker;
        }
        });
        tracking_count(Object.keys(markerStore).length, convertTimestamp(state.time));
    }
}
// currently tracked aircrafts
function tracking_count(track, time) {
    document.getElementById('cur-tracking').innerHTML = track;
    document.getElementById('last-update').innerHTML = time;

}

// Update Linfe info card
function plane_info(state) {
    var content = '<div class="card-content white-text">' +
        '<span class="card-title">Live Info: ICAO24 '+state[0]+'</span> ' +
    '<p>callsign: ' + handleempty(state[1]) + '</p>' +
    '<p>Country: ' + state[2] + '</p>' +
    '<p>Velocity: ' + state[9] + ' m/s</p>' +
    '<p>Verticale Rate: ' + state[11] + ' m/s</p>' +
    '<p>Squawk: ' + handleempty(state[14]) + '</p>' +
    '<p>Landed: ' + state[8] + '</p>' +
    '<p>Barom Altitude: ' + handleempty(state[13]) + 'm</p>' +
    '<p>heading: ' + state[10] + '°</p>' +
        '<div class="card-action"> ' +
        '<a href="https://opensky-network.org/network/explorer?icao24='+state[0]+'">More info about this flight</a> ' +
        '</div>';
    document.getElementById('plane-live').innerHTML = content;
}

// update Aircraft info card
function aircraft(icao) {
    $.get(
        "http://localhost:8080/aircraft/"+ icao ,
        function(data) {
                content = '<div class="card-content white-text">' +
                    '<span class="card-title">Aircraft Info: ICAO 24 ' + data.icao + '</span> ' +
                    '<p>Registration: ' + handleempty(data.registration) + '</p>' +
                    '<p>manufacturer icao: ' + handleempty(data.manufacturericao) + '</p>' +
                    '<p>manufacturer name: ' + handleempty(data.manufacturername) + '</p>' +
                    '<p>model: ' + handleempty(data.model) + '</p>' +
                    '<p>Owner: ' + handleempty(data.owner) + '</p>' +
                    '<p>registered until: ' + handleempty(data.reguntil) + '</p>' +
                    '<p>Built: ' + handleempty(data.built) + '</p>' +
                    '<p>operator: ' + handleempty(data.operator) + '</p>'+
                    '<div class="card-action"> ' +
                    '<a href="https://opensky-network.org/aircraft-profile?icao24='+data.icao+'">More info about this plane</a> ' +
                    '</div>';
                document.getElementById('plane-info').innerHTML = content;


        }, "json"
    ).fail(document.getElementById('plane-info').innerHTML = ' <div class="card-content white-text"><span class="card-title">No record found for ICAO 24 ' + icao + '</span></div> ' );
}

// in case no value, return N/A!
function handleempty(s){
    if(!s || s=="") return 'N/A';
    return s;
}