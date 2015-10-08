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
	 * Collapses the navbar on mobile devices.
	 */
	$scope.collapseNavbar = function() {
		angular.element(document.querySelector('.navbar-collapse'))
				.removeClass('in');
	};
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

zachariliusApp.controllers.controller('MapBreweryCtrl', function($scope){
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
	$scope.neighborhoodName = "";
	$scope.onMouseover = function(event) {
		$scope.neighborhoodName = event.feature.getProperty('NAME');
		console.log($scope.neighborhoodName);
	};
});

zachariliusApp.controllers.controller('ProjectsCtrl', function($scope){
	$scope.p1isActive = "desktop";
	$scope.p2isActive = "desktop";

	$scope.p1image = '/images/project/odd-job_DesktopView.png';
	$scope.p2image = '/images/project/odd-job_DesktopView.png';
	
	$scope.isActive = function(project, type){
		if(project === 'p1'){
			if(type === $scope.p1isActive){
				console.log("active: " + project + type)
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