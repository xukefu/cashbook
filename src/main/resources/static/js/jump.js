function jumpPage(url) {
    const authorization = encodeURIComponent(window.localStorage.getItem("tokenHead") + " " + window.localStorage.getItem("token"));
    window.location.href = "/" + url + "?Authorization=" + authorization;
}