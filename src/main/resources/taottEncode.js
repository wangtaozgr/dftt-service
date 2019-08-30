var utf8Encode = function(e) {
    e = (e + "").replace(/\r\n/g, "\n").replace(/\r/g, "\n");
    var t, r, n, a = "", i = 0;
    for (t = r = 0,
    i = e.length,
    n = 0; i > n; n++) {
        var s = e.charCodeAt(n)
          , o = null;
        128 > s ? r++ : o = s > 127 && 2048 > s ? String.fromCharCode(s >> 6 | 192, 63 & s | 128) : String.fromCharCode(s >> 12 | 224, s >> 6 & 63 | 128, 63 & s | 128),
        null !== o && (r > t && (a += e.substring(t, r)),
        a += o,
        t = r = n + 1)
    }
    return r > t && (a += e.substring(t, e.length)),a
}

function base64Encode(e) {
    var t, r, n, a, i, s, o, c, l = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", u = 0, d = 0, f = "", p = [];
    if (!e)
        return e;
    e = utf8Encode(e);
    do
        t = e.charCodeAt(u++),
        r = e.charCodeAt(u++),
        n = e.charCodeAt(u++),
        c = t << 16 | r << 8 | n,
        a = c >> 18 & 63,
        i = c >> 12 & 63,
        s = c >> 6 & 63,
        o = 63 & c,
        p[d++] = l.charAt(a) + l.charAt(i) + l.charAt(s) + l.charAt(o);
    while (u < e.length);switch (f = p.join(""),
    e.length % 3) {
    case 1:
        f = f.slice(0, -2) + "==";
        break;
    case 2:
        f = f.slice(0, -1) + "="
    }
    return encodeURIComponent(f)
}