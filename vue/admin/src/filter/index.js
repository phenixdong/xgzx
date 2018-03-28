// 字段翻译 
function translate(old,refSource,key = 'key',value = "value"){
  for(var item of refSource){
    if(item[key] == old)
      return item[value]
  }
  return old
}

function formatIndentify(val){
  if(val) {
    var arr = val.split("");
    arr.splice(10, 4, '****');
    return arr.join("");
  }else
    return val
}

// 秒转时分秒
function secondToTime(s) {
  var t;
  if(s > -1){
      var hour = Math.floor(s/3600);
      var min = Math.floor(s/60) % 60;
      var sec = s % 60;
      if(hour < 10) {
          t = '0'+ hour + ":";
      } else {
          t = hour + ":";
      }

      if(min < 10){t += "0";}
      t += min + ":";
      if(sec < 10){t += "0";}
      t += sec.toFixed(0);
  }
  return t;
}

function fileSizeFormat(size) {
  return (size / 1024 / 1024).toFixed(2) + " M";
}

function keepTwoNum(value) {
  value = Number(value);
  return value.toFixed(2);
}

export default {
  install:function(Vue){
    Vue.filter('translate', translate);
    Vue.filter('formatIndentify', formatIndentify);
    Vue.filter('secondToTime', secondToTime);
    Vue.filter('fileSizeFormat', fileSizeFormat);
    Vue.filter('keepTwoNum', keepTwoNum);
  }
}
