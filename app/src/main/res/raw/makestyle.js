function getStyleValue(oElm, strCssRule){
    var strValue = "";
    if(document.defaultView && document.defaultView.getComputedStyle){
        strValue = document.defaultView.getComputedStyle(oElm, "").getPropertyValue(strCssRule);
    }
    else if(oElm.currentStyle){
        strCssRule = strCssRule.replace(/\-(\w)/g, function (strMatch, p1){
            return p1.toUpperCase();
        });
        strValue = oElm.currentStyle[strCssRule];
    }
    return strValue;
}

function getStyle(oElm){
    var strValue = "";
    if(document.defaultView && document.defaultView.getComputedStyle){
        return document.defaultView.getComputedStyle(oElm, "");
    }
    else if(oElm.currentStyle){
       return oElm.currentStyle;
    }
    return strValue;
}



(function() {

    return "style:"+ getStyleValue(document.querySelector('img.avatar'),"width");
})();
