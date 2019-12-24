package com.atao.dftt.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.eclipsesource.v8.V8;

public class TaodanUtils {
	public static String getJslClearance02(String body) {
		// V8:谷歌开源的运行JavaScript脚本的库. 参数:globalAlias=window, 表示window为全局别名,
		// 告诉V8在运行JavaScript代码时, 不要从代码里找window的定义.
		V8 runtime = V8.createV8Runtime("window");
		// 将第一次请求pdf资源时获取到的字符串提取成V8可执行的JavaScript代码
		body = body.trim().replace("<script>", "").replace("</script>", "").replace(
				"eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)}))",
				"y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)})");
		// 用V8执行该段代码获取新的动态JavaScript脚本
		String result = runtime.executeStringScript(body);

		// 获取 jsl_clearance 的第一段, 格式形如: 1543915851.312|0|
		String startStr = "document.cookie='";
		int i1 = result.indexOf(startStr) + startStr.length();
		int i2 = result.indexOf("|0|");
		String cookie1 = result.substring(i1, i2 + 3);
		/*
		 * 获取 jsl_clearance 的第二段,格式形如: DW2jqgJO5Bo45yYRKLlFbnqQuD0%3D。 主要原理是:
		 * 新的动态JavaScript脚本是为浏览器设置cookie, 且cookie名为__jsl_clearance
		 * 其中第一段值(格式形如:1543915851.312|0|)已经明文写好, 用字符串处理方法即可获取. 第二段则是一段JavaScript函数,
		 * 需要有V8运行返回, 该函数代码需要通过一些字符串定位, 提取出来, 交给V8运行.
		 */
		startStr = "|0|'+(function(){";
		int i3 = result.indexOf(startStr) + startStr.length();
		int i4 = result.indexOf("})()+';Expires");
		String code = result.substring(i3, i4);
		// int r = code.lastIndexOf("return");
		// code = code.substring(0,r)+code.substring(r+6);
		code = code.substring(6);
		int i5 = code.indexOf("document.createElement");
		if (i5 > -1) {
			int i6 = code.indexOf(";return", i5);
			code = code.substring(0, i5) + "'www.053666.cn'" + code.substring(i6);
		}
		String cookie2 = runtime.executeStringScript(code);
		return cookie1 + cookie2;
	}

	public static String getJslClearance(String body) {
		try {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine engine = sem.getEngineByName("js");
			body = body.trim().replace("<script>", "").replace("</script>", "").replace("eval", "return");
			// 用V8执行该段代码获取新的动态JavaScript脚本
			body = "window= {};function getOneJs() {" + body + "};";
			engine.eval(body);
			Invocable inv = (Invocable) engine;
			String result = inv.invokeFunction("getOneJs").toString();
			// 获取 jsl_clearance 的第一段, 格式形如: 1543915851.312|0|
			String startStr = "document.cookie='";
			int i1 = result.indexOf(startStr) + startStr.length();
			int i2 = result.indexOf("|0|");
			String cookie1 = result.substring(i1, i2 + 3);
			/*
			 * 获取 jsl_clearance 的第二段,格式形如: DW2jqgJO5Bo45yYRKLlFbnqQuD0%3D。 主要原理是:
			 * 新的动态JavaScript脚本是为浏览器设置cookie, 且cookie名为__jsl_clearance
			 * 其中第一段值(格式形如:1543915851.312|0|)已经明文写好, 用字符串处理方法即可获取. 第二段则是一段JavaScript函数,
			 * 需要有V8运行返回, 该函数代码需要通过一些字符串定位, 提取出来, 交给V8运行.
			 */
			startStr = "|0|'+(function(){";
			int i3 = result.indexOf(startStr) + startStr.length();
			int i4 = result.indexOf("})()+';Expires");
			String code = result.substring(i3, i4);
			int i5 = code.indexOf("document.createElement");
			if (i5 > -1) {
				int i6 = code.indexOf(";return", i5);
				code = code.substring(0, i5) + "'www.053666.cn'" + code.substring(i6);
			}
			code = "window= {};function getTwoJs() {" + code + "};";
			engine.eval(code);
			inv = (Invocable) engine;
			String cookie2 = inv.invokeFunction("getTwoJs").toString();
			return cookie1 + cookie2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void main(String[] args) throws NoSuchMethodException, ScriptException {
		String body = "<script>var x=\"@09@rOm9XFMtA3QKV7nYsPGT4lifyWwkq5vcjH2IdxUoCbhERLaz81DNB6@Th@@split@w@Bv@pathname@var@Dec@@@@0@fromCharCode@@false@a@1500@while@58@createElement@Tue@JB@onreadystatechange@@Array@D@@JgSe0upZ@try@XVSKZ@8@innerHTML@location@@DOMContentLoaded@__p@19@div@Path@@https@setTimeout@@@GMT@@@eval@catch@new@12@chars@charCodeAt@@__jsl_clearance@@@e@attachEvent@@@@9@f@@function@challenge@@else@@@toLowerCase@if@@search@captcha@BJ@g@toString@P@@length@headless@as@@return@@parseInt@1575363492@@36@@0xFF@substr@match@charAt@reverse@@replace@@cookie@hantom@RegExp@for@@03@@0xEDB88320@firstChild@String@join@document@href@@DY@1@@d@109@@@addEventListener@window@Expires@\".replace(/@*$/,\"\").split(\"@\"),y=\"a 29=3c(){27('1h.62=1h.9+1h.42.57(/[\\\\?|&]43-3d/,\\\\'\\\\')',11);61.59='31=4g.68|f|'+(3c(){4d ['47',(39+[]+[]),'16',([((+!+[])<<(+!+[]))]/(+[])+[]+[]).54(-~~~{}-~~~{}),'4',(!/!/+[[]][f]).54(-~~~{}-~~~{})+[6c['21'+'5a'+'4b']+[]][f].54(-~-~~~{}),'1e',[([((+!+[])<<(+!+[]))]+~~!!6c.4a>>((+!+[])<<(+!+[])))]+[~~!!6c.4a],'%',(-~~~{}-~~~{}+[]+[[]][f]),'8',[([((+!+[])<<(+!+[]))]+~~!!6c.4a>>((+!+[])<<(+!+[])))],'7',[~~!!6c.4a],'64%',(-~~~{}-~~~{}+[]+[[]][f]),'44',({}+[]+[[]][f]).54((+!+[]))+({}+[]+[[]][f]).54((+!+[])),'%',[(((+!+[])<<(+!+[]))^-~[])],'1a'].60('')})()+';6d=15, 5e-b-22 2:13:2g 2a;24=/;'};40((3c(){1d{4d !!6c.6b;}2e(34){4d i;}})()){61.6b('20',29,i)}3f{61.35('17',29)}\",f=function(x,y){var a=0,b=0,c=0;x=x.split(\"\");y=y||99;while((a=x.shift())&&(b=a.charCodeAt(0)-77.5))c=(Math.abs(b)<13?(b+48.5):parseInt(a,36))+y*c;return c},z=f(y.match(/\\w/g).sort(function(x,y){return f(x)-f(y)}).pop());while(z++)try{eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)}));break}catch(_){}</script>";

		String s = getJslClearance02(body);
		System.out.println(s);
	}
}
