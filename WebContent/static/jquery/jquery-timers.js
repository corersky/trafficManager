jQuery.fn.extend({everyTime:function(b,c,d,e,a){return this.each(function(){jQuery.timer.add(this,b,c,d,e,a);});},oneTime:function(a,b,c){return this.each(function(){jQuery.timer.add(this,a,b,c,1);});},stopTime:function(a,b){return this.each(function(){jQuery.timer.remove(this,a,b);});}});jQuery.extend({timer:{guid:1,global:{},regex:/^([0-9]+)\s*(.*s)?$/,powers:{ms:1,cs:10,ds:100,s:1000,das:10000,hs:100000,ks:1000000},timeParse:function(c){if(c==undefined||c==null){return null;}var a=this.regex.exec(jQuery.trim(c.toString()));if(a[2]){var b=parseInt(a[1],10);var d=this.powers[a[2]]||1;return b*d;}else{return c;}},add:function(e,c,d,g,h,b){var a=0;if(jQuery.isFunction(d)){if(!h){h=g;}g=d;d=c;}c=jQuery.timer.timeParse(c);if(typeof c!="number"||isNaN(c)||c<=0){return;}if(h&&h.constructor!=Number){b=!!h;h=0;}h=h||0;b=b||false;if(!e.$timers){e.$timers={};}if(!e.$timers[d]){e.$timers[d]={};}g.$timerID=g.$timerID||this.guid++;var f=function(){if(b&&this.inProgress){return;}this.inProgress=true;if((++a>h&&h!==0)||g.call(e,a)===false){jQuery.timer.remove(e,d,g);}this.inProgress=false;};f.$timerID=g.$timerID;if(!e.$timers[d][g.$timerID]){e.$timers[d][g.$timerID]=window.setInterval(f,c);}if(!this.global[d]){this.global[d]=[];}this.global[d].push(e);},remove:function(c,b,d){var e=c.$timers,a;if(e){if(!b){for(b in e){this.remove(c,b,d);}}else{if(e[b]){if(d){if(d.$timerID){window.clearInterval(e[b][d.$timerID]);delete e[b][d.$timerID];}}else{for(var d in e[b]){window.clearInterval(e[b][d]);delete e[b][d];}}for(a in e[b]){break;}if(!a){a=null;delete e[b];}}}for(a in e){break;}if(!a){c.$timers=null;}}}}});