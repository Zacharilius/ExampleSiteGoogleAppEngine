
/**
 * @fileoverview
 * Provides methods for the Hello Endpoints sample UI and interaction with the
 * Hello Endpoints API.
 *
 * @author danielholevoet@google.com (Dan Holevoet)
 */

/** google global namespace for Google projects. */
var google = google || {};

/** devrel namespace for Google Developer Relations projects. */
google.devrel = google.devrel || {};

/** samples namespace for DevRel sample code. */
google.devrel.zacharilius = google.devrel.zacharilius || {};

/** hello namespace for this sample. */
google.devrel.zacharilius.portfolio = google.devrel.zacharilius.portfolio || {};

/**
 * Client ID of the application (from the APIs Console).
 * @type {string}
 */
google.devrel.zacharilius.portfolio.CLIENT_ID =
    '793887692854-ttu2po83g36pi70uu16n9hdhfge54oqc.apps.googleusercontent.com';

/**
 * Scopes used by the application.
 * @type {string}
 */
google.devrel.zacharilius.portfolio.SCOPES =
    'https://www.googleapis.com/auth/userinfo.email';

/**
 * Whether or not the user is signed in.
 * @type {boolean}
 */
google.devrel.zacharilius.portfolio.signedIn = false;

/**
 * Loads the application UI after the user has completed auth.
 */
google.devrel.zacharilius.portfolio.userAuthed = function() {
  var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
    if (!resp.code) {
      google.devrel.zacharilius.portfolio.signedIn = true;
      document.getElementById('signinButton').innerHTML = 'Sign Out';
      document.getElementById('authedGreeting').disabled = false;
    }
  });
};

/**
 * Handles the auth flow, with the given value for immediate mode.
 * @param {boolean} mode Whether or not to use immediate mode.
 * @param {Function} callback Callback to call on completion.
 */
google.devrel.zacharilius.portfolio.signin = function(mode, callback) {
  gapi.auth.authorize({client_id: google.devrel.zacharilius.portfolio.CLIENT_ID,
      scope: google.devrel.zacharilius.portfolio.SCOPES, immediate: mode},
      callback);
};

/**
 * Presents the user with the authorization popup.
 */
google.devrel.zacharilius.portfolio.auth = function() {
  if (!google.devrel.zacharilius.portfolio.signedIn) {
    google.devrel.zacharilius.portfolio.signin(false,
        google.devrel.zacharilius.portfolio.userAuthed);
  } else {
    google.devrel.zacharilius.portfolio.signedIn = false;
    document.getElementById('signinButton').innerHTML = 'Sign in';
    document.getElementById('authedGreeting').disabled = true;
  }
};

/**
 * Prints a greeting to the greeting log.
 * param {Object} greeting Greeting to print.
 */
google.devrel.zacharilius.portfolio.print = function(greeting) {
  console.log(greeting.items[0].title);
  /*
  var element = document.createElement('div');
  element.classList.add('row');
  element.innerHTML = greeting.message;
  document.getElementById('outputLog').appendChild(element);
  */
};

/**
 * Gets a numbered greeting via the API.
 * @param {string} id ID of the greeting.
 */
google.devrel.zacharilius.portfolio.getGreeting = function(id) {
  gapi.client.portfolio.greetings.getGreeting({'id': id}).execute(
      function(resp) {
        if (!resp.code) {
          google.devrel.zacharilius.portfolio.print(resp);
        } else {
          window.alert(resp.message);
        }
      });
};

/**
 * Lists greetings via the API.
 */
google.devrel.zacharilius.portfolio.listGreeting = function() {
  gapi.client.portfolio.greetings.listGreeting().execute(
      function(resp) {
        if (!resp.code) {
          resp.items = resp.items || [];
          for (var i = 0; i < resp.items.length; i++) {
            google.devrel.zacharilius.portfolio.print(resp.items[i]);
          }
        }
      });
};

/**
 * Gets a greeting a specified number of times.
 * @param {string} greeting Greeting to repeat.
 * @param {string} count Number of times to repeat it.
 */
google.devrel.zacharilius.portfolio.multiplyGreeting = function(
    greeting, times) {
  gapi.client.portfolio.greetings.multiply({
      'message': greeting,
      'times': times
    }).execute(function(resp) {
      if (!resp.code) {
        google.devrel.zacharilius.portfolio.print(resp);
      }
    });
};

/**
 * Greets the current user via the API.
 */
google.devrel.zacharilius.portfolio.authedGreeting = function(id) {
  gapi.client.portfolio.blog.getMostRecent().execute(
      function(resp) {
        google.devrel.zacharilius.portfolio.print(resp);       
      });
};

/**
 * Retrieves the most recent blog entry
 */
google.devrel.zacharilius.getBlogEntry = function(){
	gapi.client.portfolio.blog.getMostRecent().execute(
		function(resp) {
			console.log("got blog entry")
			google.devrel.zacharilius.portfolio.print(resp);
			if(resp.items.length > 0){
				if(resp.items[0].hasOwnProperty('title')){
					document.getElementById('blog-title').innerHTML = resp.items[0].title;
				}
				if(resp.items[0].hasOwnProperty('subTitle')){
					document.getElementById('blog-subTitle').innerHTML = resp.items[0].subTitle;
				}
				if(resp.items[0].hasOwnProperty('body')){
					document.querySelector('#blog > .container > .paragraph').innerHTML = resp.items[0].body;
				}
			}
		});
	

};
/**
 * Enables the button callbacks in the UI.
 */
google.devrel.zacharilius.portfolio.enableButtons = function() {
  /*
  document.getElementById('getGreeting').onclick = function() {
    google.devrel.zacharilius.portfolio.getGreeting(
        document.getElementById('id').value);
  }

  document.getElementById('listGreeting').onclick = function() {
    google.devrel.zacharilius.portfolio.listGreeting();
  }

  document.getElementById('multiplyGreetings').onclick = function() {
    google.devrel.zacharilius.portfolio.multiplyGreeting(
        document.getElementById('greeting').value,
        document.getElementById('count').value);
  }
  */

  document.getElementById('authedGreeting').onclick = function() {
    google.devrel.zacharilius.portfolio.authedGreeting();
  }
  document.getElementById('signinButton').onclick = function() {
    google.devrel.zacharilius.portfolio.auth();
  }
};

/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
google.devrel.zacharilius.portfolio.init = function(apiRoot) {
  // Loads the OAuth and portfolio APIs asynchronously, and triggers login
  // when they have completed.
  var apisToLoad;
  var callback = function() {
    if (--apisToLoad == 0) {
      google.devrel.zacharilius.portfolio.enableButtons();
      google.devrel.zacharilius.portfolio.signin(true,
          google.devrel.zacharilius.portfolio.userAuthed);
      google.devrel.zacharilius.getBlogEntry();
    }
  }

  apisToLoad = 2; // must match number of calls to gapi.client.load()
  gapi.client.load('portfolio', 'v1', callback, apiRoot);
  gapi.client.load('oauth2', 'v2', callback);
};
