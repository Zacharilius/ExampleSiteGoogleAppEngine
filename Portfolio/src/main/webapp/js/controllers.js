'use strict';

/**
 * The root zachariliusApp module.
 * 
 * @type {zachariliusApp|*|{}}
 */
var zachariliusApp = zachariliusApp || {};

/**
 * @ngdoc module
 * @name odd-jobControllers
 * 
 * @description Angular module for controllers.
 * 
 */
zachariliusApp.controllers = angular.module('portfolioControllers', [ 'ui.bootstrap' ]);

zachariliusApp.controllers.controller('RootCtrl', function($scope, $location) {
	
	/**
	 * Returns if the viewLocation is the currently viewed page.
	 * 
	 * @param viewLocation
	 * @returns {boolean} true if viewLocation is the currently viewed page.
	 *          Returns false otherwise.
	 */
	$scope.isActive = function(viewLocation) {
		return viewLocation === $location.path();
	};
	
	/**
	 * Collapses the navbar on mobile devices.
	 */
	$scope.collapseNavbar = function() {
		angular.element(document.querySelector('.navbar-collapse'))
				.removeClass('in');
	};
});

zachariliusApp.controllers.controller('ProjectAppADayDetailCtrl', 
		function($scope, HTTP_ERRORS){
	
});

zachariliusApp.controllers.controller('ProjectAppADayCtrl', 
		function($scope, HTTP_ERRORS){

});

zachariliusApp.controllers.controller('ContactCtrl', 
		function ($scope, HTTP_ERRORS) {
	$scope.loading = false;
	$scope.email = $scope.email || {};

    $scope.isValidEmailForm = function (emailForm) {
        return emailForm.$valid;
    }	
	
	$scope.sendEmail = function(emailForm){
		console.log(emailForm);
		console.log($scope.email);
        if (!$scope.isValidEmailForm(emailForm)) {
            return;
        }		
		
		$scope.loading = true;
		gapi.client.portfolio.sendEmail($scope.email).
            execute(function (resp) {
                $scope.$apply(function () {
                	if(resp.error){
                		//Error when sending email
                        var errorMessage = resp.error.message || '';
                        $scope.messages = 'Failed to send the email : ' + errorMessage;
                        $scope.alertStatus = 'warning';
                    } else{
                    	console.log(resp);
                		//Email send successfully
                    	$scope.messages = "Email sent successfully";
                    	$scope.alertStatus = "success";
                        $scope.emailForm = {};
                		
                	}
                    $scope.loading = false;
                    $scope.email = {};
                });
			});
		}
});
zachariliusApp.controllers.controller('MapCtrl', function($scope){

});

zachariliusApp.controllers.controller('MapCoffeeCtrl', function($scope){
	$scope.zoom = 2;
	$scope.center = "0, 0";
	$scope.scrollwheel = "false";
	
	$scope.onMouseover = function(event) {
	    var fillArray = ['red', 'blue', 'yellow', 'green'];
	    var style = this.getFeatureStyle(event.featureId); 
	    style.fillColor = fillArray[event.featureId - 1];
	    style.fillOpacity = '0.8';
	};
	$scope.onMouseout = function(event) {
		var style = this.getFeatureStyle(event.featureId).resetAll();
	};
});

zachariliusApp.controllers.controller('MapNeighborhoodCtrl', function($scope){
	$scope.zoom = 11;
	$scope.center = "47.6097, -122.3331";
	$scope.scrollwheel = "false";
	
	$scope.neighborhoodName = " ";
	$scope.onMouseover = function(event) {
		$scope.neighborhoodName = event.feature.getProperty('NAME');
		console.log($scope.neighborhoodName);
	};
});
zachariliusApp.controllers.controller('StarbucksController', function($scope, $http, StreetView) {
	$scope.zoom = 11;
	$scope.center = "47.6097, -122.3331";
	$scope.scrollwheel = "false";
	
	$scope.map;
	$scope.stores = [];
	$scope.$on('mapInitialized', function(event, evtMap) {
		var map = evtMap;
		$scope.map = map;
		console.log('loading scripts/starbucks.json');
		$http.get('/geoJson/starbucks.json').success( function(stores) {
			for (var i=0; i<stores.length; i++) {
				var store = stores[i];
				store.position = new google.maps.LatLng(store.latitude,store.longitude);
				store.title = store.name;
	
				var marker = new google.maps.Marker(store);
				google.maps.event.addListener(marker, 'click', function() {
					$scope.store = this;
					StreetView.getPanorama(map).then(function(panoId) {
						$scope.panoId = panoId;
					});
					map.setZoom(18);
					map.setCenter(this.getPosition());
					$scope.storeInfo.show();
				});
				google.maps.event.addListener(map, 'click', function() {
					$scope.storeInfo.hide();
				});
	
				$scope.stores.push(marker); 
			}
			console.log('finished loading scripts/starbucks.json', '$scope.stores', $scope.stores.length);
			$scope.markerClusterer = new MarkerClusterer(map, $scope.stores, {});
		});
	});
	$scope.showStreetView = function() {
		StreetView.setPanorama(map, $scope.panoId);
		$scope.storeInfo.hide();
	};
	$scope.showHybridView = function() {
		map.setMapTypeId(google.maps.MapTypeId.HYBRID);
		map.setTilt(45);
		$scope.storeInfo.hide();
	}
});
zachariliusApp.controllers.controller('SeattleBreweriesController', function($scope, $http, StreetView) {
	$scope.zoom = 11;
	$scope.center = "47.6097, -122.3331";
	$scope.scrollwheel = "false";
	
	$scope.map;
	$scope.breweries = [];
	$scope.$on('mapInitialized', function(event, evtMap) {
		var map = evtMap;
		$scope.map = map;
		console.log('loading geoJson/seattleBreweries.json');
		$http.get('/geoJson/seattleBreweries.json').success( function(breweries) {
			console.log(breweries);
			console.log(breweries.breweries.length);
			for (var i=0; i<breweries.breweries.length; i++) {
				var brewery = breweries.breweries[i];
				brewery.position = new google.maps.LatLng(brewery.latitude,brewery.longitude);
				brewery.title = brewery.name;
				brewery.website = brewery.url;

				var marker = new google.maps.Marker(brewery);
				google.maps.event.addListener(marker, 'click', function() {
					$scope.brewery = this;
					StreetView.getPanorama(map).then(function(panoId) {
						$scope.panoId = panoId;
					});
					map.setZoom(18);
					map.setCenter(this.getPosition());
				});
				google.maps.event.addListener(map, 'click', function() {
					$scope.storeInfo.hide();
				});
	
				$scope.breweries.push(marker); 
			}
			console.log('finished loading geoJson/seattleBreweries.json', '$scope.breweries', $scope.breweries.length);
			$scope.markerClusterer = new MarkerClusterer(map, $scope.breweries, {});
		});
	});
	$scope.showStreetView = function() {
		StreetView.setPanorama(map, $scope.panoId);
		$scope.storeInfo.hide();
	};
	$scope.showHybridView = function() {
		map.setMapTypeId(google.maps.MapTypeId.HYBRID);
		map.setTilt(45);
		$scope.storeInfo.hide();
	}
});
zachariliusApp.controllers.controller('ProjectsCtrl', function($scope){
	$scope.p1isActive = "desktop";
	$scope.p2isActive = "desktop";

	$scope.p1image = '/images/project/odd-job_DesktopView.png';
	$scope.p2image = '/images/project/odd-job_DesktopView.png';
	
	$scope.isActive = function(project, type){
		if(project === 'p1'){
			if(type === $scope.p1isActive){
				return 'active';
			};
		}
		else if(project === 'p2'){
			if(type === $scope.p2isActive){
				return 'active';
			};
		};
	};
	
	$scope.setImage = function(project, type){
		if(project === 'p1'){
			if(type === 'desktop'){
				$scope.p1image = '/images/project/odd-job_DesktopView.png';
				$scope.p1isActive = "desktop";
			}
			else if(type === 'tablet'){
				$scope.p1image = '/images/project/odd-job_TabletView.png';
				$scope.p1isActive = "tablet";
			}	
			else if(type === 'mobile'){
				$scope.p1image = '/images/project/odd-job_MobileView.png';
				$scope.p1isActive = "mobile";
			}		
		}
		else if(project === 'p2'){
			if(type === 'desktop'){
				$scope.p2image = '/images/project/odd-job_DesktopView.png';
				$scope.p2isActive = "desktop";
			}
			else if(type === 'tablet'){
				$scope.p2image = '/images/project/odd-job_TabletView.png';
				$scope.p2isActive = "tablet";
			}	
			else if(type === 'mobile'){
				$scope.p2image = '/images/project/odd-job_MobileView.png';
				$scope.p2isActive = "mobile";
			}		
		};	
	};
});

/**
 * @ngdoc controller
 * @name RootCtrl
 * 
 * @description The root controller having a scope of the body element and
 *              methods used in the application wide such as user
 *              authentications.
 * 
 */
zachariliusApp.controllers.controller('RootCtrl', function($scope, $location,
		oauth2Provider) {

	/**
	 * Returns if the viewLocation is the currently viewed page.
	 * 
	 * @param viewLocation
	 * @returns {boolean} true if viewLocation is the currently viewed page.
	 *          Returns false otherwise.
	 */
	$scope.isActive = function(viewLocation) {
		return viewLocation === $location.path();
	};

	/**
	 * Returns the OAuth2 signedIn state.
	 * 
	 * @returns {oauth2Provider.signedIn|*} true if siendIn, false otherwise.
	 */
	$scope.getSignedInState = function() {
		return oauth2Provider.signedIn;
	};

	/**
	 * Calls the OAuth2 authentication method.
	 */
	$scope.signIn = function() {
		oauth2Provider.signIn(function() {
			gapi.client.oauth2.userinfo.get().execute(function(resp) {
				$scope.$apply(function() {
					if (resp.email) {
						oauth2Provider.signedIn = true;
						$scope.alertStatus = 'success';
						$scope.rootMessages = 'Logged in with ' + resp.email;
						console.log(resp);
						$scope.$root.profilePicture = resp.picture;
					}
				});
			});
		});
	};

	/**
	 * Render the signInButton and restore the credential if it's stored in the
	 * cookie. (Just calling this to restore the credential from the stored
	 * cookie. So hiding the signInButton immediately after the rendering)
	 */
	$scope.initSignInButton = function() {
		gapi.signin.render('signInButton', {
			'callback' : function() {
				jQuery('#signInButton button').attr('disabled', 'true').css(
						'cursor', 'default');
				if (gapi.auth.getToken() && gapi.auth.getToken().access_token) {
					$scope.$apply(function() {
						oauth2Provider.signedIn = true;
					});
				}
			},
			'clientid' : oauth2Provider.CLIENT_ID,
			'cookiepolicy' : 'single_host_origin',
			'scope' : oauth2Provider.SCOPES
		});
	};

	/**
	 * Logs out the user.
	 */
	$scope.signOut = function() {
		oauth2Provider.signOut();
		$scope.alertStatus = 'success';
		$scope.rootMessages = 'Logged out';
	};

	/**
	 * Collapses the navbar on mobile devices.
	 */
	$scope.collapseNavbar = function() {
		angular.element(document.querySelector('.navbar-collapse'))
				.removeClass('in');
	};

});

/**
 * @ngdoc controller
 * @name OAuth2LoginModalCtrl
 * 
 * @description The controller for the modal dialog that is shown when an user
 *              needs to login to achive some functions.
 * 
 */
zachariliusApp.controllers.controller('OAuth2LoginModalCtrl', function($scope,
		$modalInstance, $rootScope, oauth2Provider) {
	$scope.singInViaModal = function() {
		oauth2Provider.signIn(function() {
			gapi.client.oauth2.userinfo.get().execute(function(resp) {
				$scope.$root.$apply(function() {
					oauth2Provider.signedIn = true;
					$scope.$root.alertStatus = 'success';
					$scope.$root.rootMessages = 'Logged in with ' + resp.email;
					$scope.$root.profilePicture = resp.picture;
					console.log($scope.$root.profilePicture);
				});

				$modalInstance.close();
			});
		});
	};
});