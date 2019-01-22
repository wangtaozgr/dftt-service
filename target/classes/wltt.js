var window = window || {};
var navigator = navigator|| {};
var $ = window.$ || {};     

var ERROR = "input is invalid type";
var WINDOW = "object" == typeof window
    , root = WINDOW ? window : {};
  root.JS_MD5_NO_WINDOW && (WINDOW = !1);
  var WEB_WORKER = !WINDOW && "object" == typeof self
    , NODE_JS = !root.JS_MD5_NO_NODE_JS && "object" == typeof process && process.versions && process.versions.node;
  NODE_JS ? root = global : WEB_WORKER && (root = self);
  var COMMON_JS = !root.JS_MD5_NO_COMMON_JS && "object" == typeof module && module.exports, ARRAY_BUFFER = !root.JS_MD5_NO_ARRAY_BUFFER && "undefined" != typeof ArrayBuffer, HEX_CHARS = "0123456789abcdef".split(""), EXTRA = [128, 32768, 8388608, -2147483648], SHIFT = [0, 8, 16, 24], OUTPUT_TYPES = ["hex", "array", "digest", "buffer", "arrayBuffer", "base64"], BASE64_ENCODE_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split(""), blocks = [], buffer8;
  if (ARRAY_BUFFER) {
      var buffer = new ArrayBuffer(68);
      buffer8 = new Uint8Array(buffer),
      blocks = new Uint32Array(buffer)
  }
            


function Md5(e) {
    if (e)
                    blocks[0] = blocks[16] = blocks[1] = blocks[2] = blocks[3] = blocks[4] = blocks[5] = blocks[6] = blocks[7] = blocks[8] = blocks[9] = blocks[10] = blocks[11] = blocks[12] = blocks[13] = blocks[14] = blocks[15] = 0,
                    this.blocks = blocks,
                    this.buffer8 = buffer8;
                else if (ARRAY_BUFFER) {
                    var t = new ArrayBuffer(68);
                    this.buffer8 = new Uint8Array(t),
                    this.blocks = new Uint32Array(t)
                } else
                    this.blocks = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                this.h0 = this.h1 = this.h2 = this.h3 = this.start = this.bytes = this.hBytes = 0,
                this.finalized = this.hashed = !1,
                this.first = !0
}
            Md5.prototype.update = function(e) {
                if (!this.finalized) {
                    var t, n = typeof e;
                    if ("string" !== n) {
                        if ("object" !== n)
                            throw ERROR;
                        if (null === e)
                            throw ERROR;
                        if (ARRAY_BUFFER && e.constructor === ArrayBuffer)
                            e = new Uint8Array(e);
                        else if (!(Array.isArray(e) || ARRAY_BUFFER && ArrayBuffer.isView(e)))
                            throw ERROR;
                        t = !0
                    }
                    for (var r, a, i = 0, o = e.length, s = this.blocks, u = this.buffer8; i < o; ) {
                        if (this.hashed && (this.hashed = !1,
                        s[0] = s[16],
                        s[16] = s[1] = s[2] = s[3] = s[4] = s[5] = s[6] = s[7] = s[8] = s[9] = s[10] = s[11] = s[12] = s[13] = s[14] = s[15] = 0),
                        t)
                            if (ARRAY_BUFFER)
                                for (a = this.start; i < o && a < 64; ++i)
                                    u[a++] = e[i];
                            else
                                for (a = this.start; i < o && a < 64; ++i)
                                    s[a >> 2] |= e[i] << SHIFT[3 & a++];
                        else if (ARRAY_BUFFER)
                            for (a = this.start; i < o && a < 64; ++i)
                                (r = e.charCodeAt(i)) < 128 ? u[a++] = r : r < 2048 ? (u[a++] = 192 | r >> 6,
                                u[a++] = 128 | 63 & r) : r < 55296 || r >= 57344 ? (u[a++] = 224 | r >> 12,
                                u[a++] = 128 | r >> 6 & 63,
                                u[a++] = 128 | 63 & r) : (r = 65536 + ((1023 & r) << 10 | 1023 & e.charCodeAt(++i)),
                                u[a++] = 240 | r >> 18,
                                u[a++] = 128 | r >> 12 & 63,
                                u[a++] = 128 | r >> 6 & 63,
                                u[a++] = 128 | 63 & r);
                        else
                            for (a = this.start; i < o && a < 64; ++i)
                                (r = e.charCodeAt(i)) < 128 ? s[a >> 2] |= r << SHIFT[3 & a++] : r < 2048 ? (s[a >> 2] |= (192 | r >> 6) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | 63 & r) << SHIFT[3 & a++]) : r < 55296 || r >= 57344 ? (s[a >> 2] |= (224 | r >> 12) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | r >> 6 & 63) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | 63 & r) << SHIFT[3 & a++]) : (r = 65536 + ((1023 & r) << 10 | 1023 & e.charCodeAt(++i)),
                                s[a >> 2] |= (240 | r >> 18) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | r >> 12 & 63) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | r >> 6 & 63) << SHIFT[3 & a++],
                                s[a >> 2] |= (128 | 63 & r) << SHIFT[3 & a++]);
                        this.lastByteIndex = a,
                        this.bytes += a - this.start,
                        a >= 64 ? (this.start = a - 64,
                        this.hash(),
                        this.hashed = !0) : this.start = a
                    }
                    return this.bytes > 4294967295 && (this.hBytes += this.bytes / 4294967296 << 0,
                    this.bytes = this.bytes % 4294967296),
                    this
                }
            }
            ,
            Md5.prototype.finalize = function() {
                if (!this.finalized) {
                    this.finalized = !0;
                    var e = this.blocks
                      , t = this.lastByteIndex;
                    e[t >> 2] |= EXTRA[3 & t],
                    t >= 56 && (this.hashed || this.hash(),
                    e[0] = e[16],
                    e[16] = e[1] = e[2] = e[3] = e[4] = e[5] = e[6] = e[7] = e[8] = e[9] = e[10] = e[11] = e[12] = e[13] = e[14] = e[15] = 0),
                    e[14] = this.bytes << 3,
                    e[15] = this.hBytes << 3 | this.bytes >>> 29,
                    this.hash()
                }
            }
            ,
            Md5.prototype.hash = function() {
                var e, t, n, r, a, i, o = this.blocks;
                this.first ? t = ((t = ((e = ((e = o[0] - 680876937) << 7 | e >>> 25) - 271733879 << 0) ^ (n = ((n = (-271733879 ^ (r = ((r = (-1732584194 ^ 2004318071 & e) + o[1] - 117830708) << 12 | r >>> 20) + e << 0) & (-271733879 ^ e)) + o[2] - 1126478375) << 17 | n >>> 15) + r << 0) & (r ^ e)) + o[3] - 1316259209) << 22 | t >>> 10) + n << 0 : (e = this.h0,
                t = this.h1,
                n = this.h2,
                t = ((t += ((e = ((e += ((r = this.h3) ^ t & (n ^ r)) + o[0] - 680876936) << 7 | e >>> 25) + t << 0) ^ (n = ((n += (t ^ (r = ((r += (n ^ e & (t ^ n)) + o[1] - 389564586) << 12 | r >>> 20) + e << 0) & (e ^ t)) + o[2] + 606105819) << 17 | n >>> 15) + r << 0) & (r ^ e)) + o[3] - 1044525330) << 22 | t >>> 10) + n << 0),
                t = ((t += ((e = ((e += (r ^ t & (n ^ r)) + o[4] - 176418897) << 7 | e >>> 25) + t << 0) ^ (n = ((n += (t ^ (r = ((r += (n ^ e & (t ^ n)) + o[5] + 1200080426) << 12 | r >>> 20) + e << 0) & (e ^ t)) + o[6] - 1473231341) << 17 | n >>> 15) + r << 0) & (r ^ e)) + o[7] - 45705983) << 22 | t >>> 10) + n << 0,
                t = ((t += ((e = ((e += (r ^ t & (n ^ r)) + o[8] + 1770035416) << 7 | e >>> 25) + t << 0) ^ (n = ((n += (t ^ (r = ((r += (n ^ e & (t ^ n)) + o[9] - 1958414417) << 12 | r >>> 20) + e << 0) & (e ^ t)) + o[10] - 42063) << 17 | n >>> 15) + r << 0) & (r ^ e)) + o[11] - 1990404162) << 22 | t >>> 10) + n << 0,
                t = ((t += ((e = ((e += (r ^ t & (n ^ r)) + o[12] + 1804603682) << 7 | e >>> 25) + t << 0) ^ (n = ((n += (t ^ (r = ((r += (n ^ e & (t ^ n)) + o[13] - 40341101) << 12 | r >>> 20) + e << 0) & (e ^ t)) + o[14] - 1502002290) << 17 | n >>> 15) + r << 0) & (r ^ e)) + o[15] + 1236535329) << 22 | t >>> 10) + n << 0,
                t = ((t += ((r = ((r += (t ^ n & ((e = ((e += (n ^ r & (t ^ n)) + o[1] - 165796510) << 5 | e >>> 27) + t << 0) ^ t)) + o[6] - 1069501632) << 9 | r >>> 23) + e << 0) ^ e & ((n = ((n += (e ^ t & (r ^ e)) + o[11] + 643717713) << 14 | n >>> 18) + r << 0) ^ r)) + o[0] - 373897302) << 20 | t >>> 12) + n << 0,
                t = ((t += ((r = ((r += (t ^ n & ((e = ((e += (n ^ r & (t ^ n)) + o[5] - 701558691) << 5 | e >>> 27) + t << 0) ^ t)) + o[10] + 38016083) << 9 | r >>> 23) + e << 0) ^ e & ((n = ((n += (e ^ t & (r ^ e)) + o[15] - 660478335) << 14 | n >>> 18) + r << 0) ^ r)) + o[4] - 405537848) << 20 | t >>> 12) + n << 0,
                t = ((t += ((r = ((r += (t ^ n & ((e = ((e += (n ^ r & (t ^ n)) + o[9] + 568446438) << 5 | e >>> 27) + t << 0) ^ t)) + o[14] - 1019803690) << 9 | r >>> 23) + e << 0) ^ e & ((n = ((n += (e ^ t & (r ^ e)) + o[3] - 187363961) << 14 | n >>> 18) + r << 0) ^ r)) + o[8] + 1163531501) << 20 | t >>> 12) + n << 0,
                t = ((t += ((r = ((r += (t ^ n & ((e = ((e += (n ^ r & (t ^ n)) + o[13] - 1444681467) << 5 | e >>> 27) + t << 0) ^ t)) + o[2] - 51403784) << 9 | r >>> 23) + e << 0) ^ e & ((n = ((n += (e ^ t & (r ^ e)) + o[7] + 1735328473) << 14 | n >>> 18) + r << 0) ^ r)) + o[12] - 1926607734) << 20 | t >>> 12) + n << 0,
                t = ((t += ((i = (r = ((r += ((a = t ^ n) ^ (e = ((e += (a ^ r) + o[5] - 378558) << 4 | e >>> 28) + t << 0)) + o[8] - 2022574463) << 11 | r >>> 21) + e << 0) ^ e) ^ (n = ((n += (i ^ t) + o[11] + 1839030562) << 16 | n >>> 16) + r << 0)) + o[14] - 35309556) << 23 | t >>> 9) + n << 0,
                t = ((t += ((i = (r = ((r += ((a = t ^ n) ^ (e = ((e += (a ^ r) + o[1] - 1530992060) << 4 | e >>> 28) + t << 0)) + o[4] + 1272893353) << 11 | r >>> 21) + e << 0) ^ e) ^ (n = ((n += (i ^ t) + o[7] - 155497632) << 16 | n >>> 16) + r << 0)) + o[10] - 1094730640) << 23 | t >>> 9) + n << 0,
                t = ((t += ((i = (r = ((r += ((a = t ^ n) ^ (e = ((e += (a ^ r) + o[13] + 681279174) << 4 | e >>> 28) + t << 0)) + o[0] - 358537222) << 11 | r >>> 21) + e << 0) ^ e) ^ (n = ((n += (i ^ t) + o[3] - 722521979) << 16 | n >>> 16) + r << 0)) + o[6] + 76029189) << 23 | t >>> 9) + n << 0,
                t = ((t += ((i = (r = ((r += ((a = t ^ n) ^ (e = ((e += (a ^ r) + o[9] - 640364487) << 4 | e >>> 28) + t << 0)) + o[12] - 421815835) << 11 | r >>> 21) + e << 0) ^ e) ^ (n = ((n += (i ^ t) + o[15] + 530742520) << 16 | n >>> 16) + r << 0)) + o[2] - 995338651) << 23 | t >>> 9) + n << 0,
                t = ((t += ((r = ((r += (t ^ ((e = ((e += (n ^ (t | ~r)) + o[0] - 198630844) << 6 | e >>> 26) + t << 0) | ~n)) + o[7] + 1126891415) << 10 | r >>> 22) + e << 0) ^ ((n = ((n += (e ^ (r | ~t)) + o[14] - 1416354905) << 15 | n >>> 17) + r << 0) | ~e)) + o[5] - 57434055) << 21 | t >>> 11) + n << 0,
                t = ((t += ((r = ((r += (t ^ ((e = ((e += (n ^ (t | ~r)) + o[12] + 1700485571) << 6 | e >>> 26) + t << 0) | ~n)) + o[3] - 1894986606) << 10 | r >>> 22) + e << 0) ^ ((n = ((n += (e ^ (r | ~t)) + o[10] - 1051523) << 15 | n >>> 17) + r << 0) | ~e)) + o[1] - 2054922799) << 21 | t >>> 11) + n << 0,
                t = ((t += ((r = ((r += (t ^ ((e = ((e += (n ^ (t | ~r)) + o[8] + 1873313359) << 6 | e >>> 26) + t << 0) | ~n)) + o[15] - 30611744) << 10 | r >>> 22) + e << 0) ^ ((n = ((n += (e ^ (r | ~t)) + o[6] - 1560198380) << 15 | n >>> 17) + r << 0) | ~e)) + o[13] + 1309151649) << 21 | t >>> 11) + n << 0,
                t = ((t += ((r = ((r += (t ^ ((e = ((e += (n ^ (t | ~r)) + o[4] - 145523070) << 6 | e >>> 26) + t << 0) | ~n)) + o[11] - 1120210379) << 10 | r >>> 22) + e << 0) ^ ((n = ((n += (e ^ (r | ~t)) + o[2] + 718787259) << 15 | n >>> 17) + r << 0) | ~e)) + o[9] - 343485551) << 21 | t >>> 11) + n << 0,
                this.first ? (this.h0 = e + 1732584193 << 0,
                this.h1 = t - 271733879 << 0,
                this.h2 = n - 1732584194 << 0,
                this.h3 = r + 271733878 << 0,
                this.first = !1) : (this.h0 = this.h0 + e << 0,
                this.h1 = this.h1 + t << 0,
                this.h2 = this.h2 + n << 0,
                this.h3 = this.h3 + r << 0)
            }
            ,
            Md5.prototype.hex = function() {
                this.finalize();
                var e = this.h0
                  , t = this.h1
                  , n = this.h2
                  , r = this.h3;
                return HEX_CHARS[e >> 4 & 15] + HEX_CHARS[15 & e] + HEX_CHARS[e >> 12 & 15] + HEX_CHARS[e >> 8 & 15] + HEX_CHARS[e >> 20 & 15] + HEX_CHARS[e >> 16 & 15] + HEX_CHARS[e >> 28 & 15] + HEX_CHARS[e >> 24 & 15] + HEX_CHARS[t >> 4 & 15] + HEX_CHARS[15 & t] + HEX_CHARS[t >> 12 & 15] + HEX_CHARS[t >> 8 & 15] + HEX_CHARS[t >> 20 & 15] + HEX_CHARS[t >> 16 & 15] + HEX_CHARS[t >> 28 & 15] + HEX_CHARS[t >> 24 & 15] + HEX_CHARS[n >> 4 & 15] + HEX_CHARS[15 & n] + HEX_CHARS[n >> 12 & 15] + HEX_CHARS[n >> 8 & 15] + HEX_CHARS[n >> 20 & 15] + HEX_CHARS[n >> 16 & 15] + HEX_CHARS[n >> 28 & 15] + HEX_CHARS[n >> 24 & 15] + HEX_CHARS[r >> 4 & 15] + HEX_CHARS[15 & r] + HEX_CHARS[r >> 12 & 15] + HEX_CHARS[r >> 8 & 15] + HEX_CHARS[r >> 20 & 15] + HEX_CHARS[r >> 16 & 15] + HEX_CHARS[r >> 28 & 15] + HEX_CHARS[r >> 24 & 15]
            }
            ,
            Md5.prototype.toString = Md5.prototype.hex,
            Md5.prototype.digest = function() {
                this.finalize();
                var e = this.h0
                  , t = this.h1
                  , n = this.h2
                  , r = this.h3;
                return [255 & e, e >> 8 & 255, e >> 16 & 255, e >> 24 & 255, 255 & t, t >> 8 & 255, t >> 16 & 255, t >> 24 & 255, 255 & n, n >> 8 & 255, n >> 16 & 255, n >> 24 & 255, 255 & r, r >> 8 & 255, r >> 16 & 255, r >> 24 & 255]
            }
            ,
            Md5.prototype.array = Md5.prototype.digest,
            Md5.prototype.arrayBuffer = function() {
                this.finalize();
                var e = new ArrayBuffer(16)
                  , t = new Uint32Array(e);
                return t[0] = this.h0,
                t[1] = this.h1,
                t[2] = this.h2,
                t[3] = this.h3,
                e
            }
            ,
            Md5.prototype.buffer = Md5.prototype.arrayBuffer,
            Md5.prototype.base64 = function() {
                for (var e, t, n, r = "", a = this.array(), i = 0; i < 15; )
                    e = a[i++],
                    t = a[i++],
                    n = a[i++],
                    r += BASE64_ENCODE_CHAR[e >>> 2] + BASE64_ENCODE_CHAR[63 & (e << 4 | t >>> 4)] + BASE64_ENCODE_CHAR[63 & (t << 2 | n >>> 6)] + BASE64_ENCODE_CHAR[63 & n];
                return e = a[i],
                r += BASE64_ENCODE_CHAR[e >>> 2] + BASE64_ENCODE_CHAR[e << 4 & 63] + "=="
            };
            
function getAppSign(s){
	 return new Md5(!0).update(s)["hex"]()
}