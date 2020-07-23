package com.mxkj.yuanyintang.mainui.search.fragment;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class MusicSearchBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"count":{"musicCount":30,"musicianCount":2,"songCount":3,"topicCount":36,"topicCount_text":"36"},"data":[{"id":9640,"title":"测试02","counts":16,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"7F15A0950ED85D1A102A4C9DC1D92C5B","category":6,"playtime":"00:00","isdown":1,"video":"0138675cb6fc18342c9a55c2d89d2bda","mv":"5DAA4CCF59B2E19560798FD4BCAE0473","is_collection":0,"song_id":"","collection":0,"counts_text":"16","imgpic_link":"http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3","imgpic_info":{"ext":"jpg","w":"3120","h":"4160","size":"5256330","is_long":"0","md5":"7F15A0950ED85D1A102A4C9DC1D92C5B","link":"http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3"},"video_link":"http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3","video_info":{"ext":"mp3","size":"4030424","playtime":"04:11","bitrate":"128","md5":"0138675cb6fc18342c9a55c2d89d2bda","link":"http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":92323169,"playtime":"00:43","bitrate":"17130","height":1088,"width":1920,"fps":30,"md5":"5DAA4CCF59B2E19560798FD4BCAE0473","link":"http://testapi.imxkj.com//video/5DAA4CCF59B2E19560798FD4BCAE0473.mp4?log_at=3"},"collection_text":"0"},{"id":9639,"title":"测试1","counts":16,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"99a5d4941a0c9bb5d02a4153ca191f4a","category":6,"playtime":"00:00","isdown":1,"video":"89fc0c3a28e6ac8e12f1818c435f92c6","mv":"C114858627F589CF430F84EF8DD0D481","is_collection":0,"song_id":"","collection":0,"counts_text":"16","imgpic_link":"http://testapi.imxkj.com//image/99a5d4941a0c9bb5d02a4153ca191f4a/3","imgpic_info":{"ext":"jpg","w":"584","h":"823","size":"65990","is_long":"0","md5":"99a5d4941a0c9bb5d02a4153ca191f4a","link":"http://testapi.imxkj.com//image/99a5d4941a0c9bb5d02a4153ca191f4a/3"},"video_link":"http://testapi.imxkj.com//music/89fc0c3a28e6ac8e12f1818c435f92c6.mp3?log_at=3","video_info":{"ext":"mp3","size":"3407341","playtime":"03:32","bitrate":"128","md5":"89fc0c3a28e6ac8e12f1818c435f92c6","link":"http://testapi.imxkj.com//music/89fc0c3a28e6ac8e12f1818c435f92c6.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":53353112,"playtime":"00:25","bitrate":"17056","height":1088,"width":1920,"fps":30,"md5":"C114858627F589CF430F84EF8DD0D481","link":"http://testapi.imxkj.com//video/C114858627F589CF430F84EF8DD0D481.mp4?log_at=3"},"collection_text":"0"},{"id":9629,"title":"测试01","counts":41,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"8271b42bf3fbba1d3c90a00d56793aba","category":20,"playtime":"00:00","isdown":1,"video":"a4a22a4a848c63338f1d234b2c1ccf6c","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"41","imgpic_link":"http://testapi.imxkj.com//image/8271b42bf3fbba1d3c90a00d56793aba/3","imgpic_info":{"ext":"png","w":"100","h":"100","size":"1641","is_long":"0","md5":"8271b42bf3fbba1d3c90a00d56793aba","link":"http://testapi.imxkj.com//image/8271b42bf3fbba1d3c90a00d56793aba/3"},"video_link":"http://testapi.imxkj.com//music/a4a22a4a848c63338f1d234b2c1ccf6c.mp3?log_at=3","video_info":{"ext":"mp3","size":"3563613","playtime":"03:42","bitrate":"128","md5":"a4a22a4a848c63338f1d234b2c1ccf6c","link":"http://testapi.imxkj.com//music/a4a22a4a848c63338f1d234b2c1ccf6c.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9628,"title":"测试","counts":6,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"2DDDF588128FA22F90AF8B3069685C54","category":19,"playtime":"00:00","isdown":1,"video":"f2cc9ce859a6a982a4772bf4b328438f","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"6","imgpic_link":"http://testapi.imxkj.com//image/2DDDF588128FA22F90AF8B3069685C54/3","imgpic_info":{"ext":"png","w":"720","h":"1280","size":"112262","is_long":"0","md5":"2DDDF588128FA22F90AF8B3069685C54","link":"http://testapi.imxkj.com//image/2DDDF588128FA22F90AF8B3069685C54/3"},"video_link":"http://testapi.imxkj.com//music/f2cc9ce859a6a982a4772bf4b328438f.mp3?log_at=3","video_info":{"ext":"mp3","size":"3197530","playtime":"03:19","bitrate":"128","md5":"f2cc9ce859a6a982a4772bf4b328438f","link":"http://testapi.imxkj.com//music/f2cc9ce859a6a982a4772bf4b328438f.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9627,"title":"测试播放","counts":10,"music_type":0,"uid":57985,"nickname":"皮大锤","imgpic":"E0F4C9AAE7131472BC28406810F558BE","category":6,"playtime":"00:00","isdown":1,"video":"1F796FCBA9867551E18521AD3EFD2989","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"10","imgpic_link":"http://testapi.imxkj.com//image/E0F4C9AAE7131472BC28406810F558BE/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"79452","is_long":"0","md5":"E0F4C9AAE7131472BC28406810F558BE","link":"http://testapi.imxkj.com//image/E0F4C9AAE7131472BC28406810F558BE/3"},"video_link":"http://testapi.imxkj.com//music/1F796FCBA9867551E18521AD3EFD2989.mp3?log_at=3","video_info":{"ext":"mp3","size":"4832631","playtime":"05:01","bitrate":"128","md5":"1F796FCBA9867551E18521AD3EFD2989","link":"http://testapi.imxkj.com//music/1F796FCBA9867551E18521AD3EFD2989.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9624,"title":"测试55","counts":9,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"3E099C51ECD50030D7F202CDD830BB59","category":5,"playtime":"00:00","isdown":1,"video":"9a00fa24dac44291df6a5fc13bf217f2","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"9","imgpic_link":"http://testapi.imxkj.com//image/3E099C51ECD50030D7F202CDD830BB59/3","imgpic_info":{"ext":"jpg","w":"2362","h":"3544","size":"3778129","is_long":"0","md5":"3E099C51ECD50030D7F202CDD830BB59","link":"http://testapi.imxkj.com//image/3E099C51ECD50030D7F202CDD830BB59/3"},"video_link":"http://testapi.imxkj.com//music/9a00fa24dac44291df6a5fc13bf217f2.mp3?log_at=3","video_info":{"ext":"mp3","size":"7864380","playtime":"03:15","bitrate":"321","md5":"9a00fa24dac44291df6a5fc13bf217f2","link":"http://testapi.imxkj.com//music/9a00fa24dac44291df6a5fc13bf217f2.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9623,"title":"测试444","counts":52,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"59b6e18e579f31da685e116f68e2f690","category":6,"playtime":"00:00","isdown":1,"video":"b313b3c52494c4ef2083c0be74181b3d","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"52","imgpic_link":"http://testapi.imxkj.com//image/59b6e18e579f31da685e116f68e2f690/3","imgpic_info":{"ext":"jpg","w":"600","h":"600","size":"44304","is_long":"0","md5":"59b6e18e579f31da685e116f68e2f690","link":"http://testapi.imxkj.com//image/59b6e18e579f31da685e116f68e2f690/3"},"video_link":"http://testapi.imxkj.com//music/b313b3c52494c4ef2083c0be74181b3d.mp3?log_at=3","video_info":{"ext":"mp3","size":"8883692","playtime":"03:41","bitrate":"321","md5":"b313b3c52494c4ef2083c0be74181b3d","link":"http://testapi.imxkj.com//music/b313b3c52494c4ef2083c0be74181b3d.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9622,"title":"测试333","counts":18,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"E61CE4B4B1CF580B15E70340849A3A64","category":5,"playtime":"00:00","isdown":1,"video":"7f9b936a650ef04029d217f473758df4","mv":"2403B6BFAA84C14DF3620F4FCFDC99CF","is_collection":0,"song_id":"","collection":0,"counts_text":"18","imgpic_link":"http://testapi.imxkj.com//image/E61CE4B4B1CF580B15E70340849A3A64/3","imgpic_info":{"ext":"jpg","w":"1279","h":"1226","size":"119141","is_long":"0","md5":"E61CE4B4B1CF580B15E70340849A3A64","link":"http://testapi.imxkj.com//image/E61CE4B4B1CF580B15E70340849A3A64/3"},"video_link":"http://testapi.imxkj.com//music/7f9b936a650ef04029d217f473758df4.mp3?log_at=3","video_info":{"ext":"mp3","size":"5643007","playtime":"03:55","bitrate":"192","md5":"7f9b936a650ef04029d217f473758df4","link":"http://testapi.imxkj.com//music/7f9b936a650ef04029d217f473758df4.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":6808381,"playtime":"00:03","bitrate":"16580","height":1088,"width":1920,"fps":30,"md5":"2403B6BFAA84C14DF3620F4FCFDC99CF","link":"http://testapi.imxkj.com//video/2403B6BFAA84C14DF3620F4FCFDC99CF.mp4?log_at=3"},"collection_text":"0"},{"id":9621,"title":"测试2222","counts":25,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"F2018C5352C1B0818F5195B8B808666A","category":19,"playtime":"00:00","isdown":1,"video":"ef8afe9d6014eac6c0365a1300e1097d","mv":"31053A65A46C0D4A94CDF3BB247413B5","is_collection":1,"song_id":"","collection":1,"counts_text":"25","imgpic_link":"http://testapi.imxkj.com//image/F2018C5352C1B0818F5195B8B808666A/3","imgpic_info":{"ext":"jpg","w":"1000","h":"1000","size":"86123","is_long":"0","md5":"F2018C5352C1B0818F5195B8B808666A","link":"http://testapi.imxkj.com//image/F2018C5352C1B0818F5195B8B808666A/3"},"video_link":"http://testapi.imxkj.com//music/ef8afe9d6014eac6c0365a1300e1097d.mp3?log_at=3","video_info":{"ext":"mp3","size":"10101371","playtime":"04:08","bitrate":"325","md5":"ef8afe9d6014eac6c0365a1300e1097d","link":"http://testapi.imxkj.com//music/ef8afe9d6014eac6c0365a1300e1097d.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":1190062,"playtime":"00:04","bitrate":"1907","height":480,"width":720,"fps":29,"md5":"31053A65A46C0D4A94CDF3BB247413B5","link":"http://testapi.imxkj.com//video/31053A65A46C0D4A94CDF3BB247413B5.mp4?log_at=3"},"collection_text":"1"},{"id":9620,"title":"测试111","counts":26,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"7c1e24cf00019d940fc4a901311171ab","category":5,"playtime":"00:00","isdown":1,"video":"fc0a406abf73e31f39cddf09146a1e36","mv":"67401DD42C2421E92C126FFA9A13219B","is_collection":0,"song_id":"","collection":0,"counts_text":"26","imgpic_link":"http://testapi.imxkj.com//image/7c1e24cf00019d940fc4a901311171ab/3","imgpic_info":{"ext":"jpg","w":"102","h":"112","size":"15396","is_long":"0","md5":"7c1e24cf00019d940fc4a901311171ab","link":"http://testapi.imxkj.com//image/7c1e24cf00019d940fc4a901311171ab/3"},"video_link":"http://testapi.imxkj.com//music/fc0a406abf73e31f39cddf09146a1e36.mp3?log_at=3","video_info":{"ext":"mp3","size":"5071076","playtime":"02:02","bitrate":"331","md5":"fc0a406abf73e31f39cddf09146a1e36","link":"http://testapi.imxkj.com//music/fc0a406abf73e31f39cddf09146a1e36.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":1195195,"playtime":"00:05","bitrate":"1875","height":480,"width":720,"fps":29,"md5":"67401DD42C2421E92C126FFA9A13219B","link":"http://testapi.imxkj.com//video/67401DD42C2421E92C126FFA9A13219B.mp4?log_at=3"},"collection_text":"0"},{"id":9603,"title":"测试","counts":0,"music_type":1,"uid":81811,"nickname":"源音塘920450","imgpic":"a838271a008cf2d8e1e53f59355789a8","category":5,"playtime":"00:00","isdown":1,"video":"412875847BD1592419DF5060C425A0B3","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"157041","is_long":"0","md5":"a838271a008cf2d8e1e53f59355789a8","link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3"},"video_link":"http://testapi.imxkj.com//music/412875847BD1592419DF5060C425A0B3.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"412875847BD1592419DF5060C425A0B3","link":"http://testapi.imxkj.com//music/412875847BD1592419DF5060C425A0B3.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9602,"title":"测试","counts":0,"music_type":0,"uid":81837,"nickname":"源音塘752566","imgpic":"0c78432405e68548035f802d8718a470","category":5,"playtime":"00:00","isdown":1,"video":"DDFFB721A31FF1838A6826BE8E1466E8","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/0c78432405e68548035f802d8718a470/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"58230","is_long":"0","md5":"0c78432405e68548035f802d8718a470","link":"http://testapi.imxkj.com//image/0c78432405e68548035f802d8718a470/3"},"video_link":"http://testapi.imxkj.com//music/DDFFB721A31FF1838A6826BE8E1466E8.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"DDFFB721A31FF1838A6826BE8E1466E8","link":"http://testapi.imxkj.com//music/DDFFB721A31FF1838A6826BE8E1466E8.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9601,"title":"测试","counts":0,"music_type":1,"uid":81735,"nickname":"钢铁侠3","imgpic":"a3d4d13cbc7b6ed59b10333f143d25a6","category":6,"playtime":"00:00","isdown":1,"video":"64D2E7AADC66D454C624B96477714B21","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"83101","is_long":"0","md5":"a3d4d13cbc7b6ed59b10333f143d25a6","link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3"},"video_link":"http://testapi.imxkj.com//music/64D2E7AADC66D454C624B96477714B21.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"64D2E7AADC66D454C624B96477714B21","link":"http://testapi.imxkj.com//music/64D2E7AADC66D454C624B96477714B21.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9600,"title":"测试005","counts":38,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"e57bdfd6eb951ac2f37091dae0cb2fc1","category":5,"playtime":"00:00","isdown":1,"video":"b59a93a69427f0874164d5bea6e547fd","mv":"3f423ca6736007d7fdce244ff0a78da0","is_collection":1,"song_id":"","collection":1,"counts_text":"38","imgpic_link":"http://testapi.imxkj.com//image/e57bdfd6eb951ac2f37091dae0cb2fc1/3","imgpic_info":{"ext":"png","w":"720","h":"1280","size":"1516613","is_long":"0","md5":"e57bdfd6eb951ac2f37091dae0cb2fc1","link":"http://testapi.imxkj.com//image/e57bdfd6eb951ac2f37091dae0cb2fc1/3"},"video_link":"http://testapi.imxkj.com//music/b59a93a69427f0874164d5bea6e547fd.mp3?log_at=3","video_info":{"ext":"mp3","size":"6089432","playtime":"02:07","bitrate":"383","md5":"b59a93a69427f0874164d5bea6e547fd","link":"http://testapi.imxkj.com//music/b59a93a69427f0874164d5bea6e547fd.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":4817470,"playtime":"00:03","bitrate":"12123","height":1088,"width":1920,"fps":16,"md5":"3f423ca6736007d7fdce244ff0a78da0","link":"http://testapi.imxkj.com//video/3f423ca6736007d7fdce244ff0a78da0.mp4?log_at=3"},"collection_text":"1"},{"id":9597,"title":"测试003","counts":129,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"6091d203f6d1dd28fe4076ccfdcd2c35","category":5,"playtime":"00:00","isdown":1,"video":"fcfed7cb7305840d6bcddc71ba626ee9","mv":"b8a19241332ec97908e9eeac8638b50c","is_collection":1,"song_id":"","collection":1,"counts_text":"129","imgpic_link":"http://testapi.imxkj.com//image/6091d203f6d1dd28fe4076ccfdcd2c35/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1920","size":"336739","is_long":"0","md5":"6091d203f6d1dd28fe4076ccfdcd2c35","link":"http://testapi.imxkj.com//image/6091d203f6d1dd28fe4076ccfdcd2c35/3"},"video_link":"http://testapi.imxkj.com//music/fcfed7cb7305840d6bcddc71ba626ee9.mp3?log_at=3","video_info":{"ext":"mp3","size":"5576714","playtime":"05:28","bitrate":"135","md5":"fcfed7cb7305840d6bcddc71ba626ee9","link":"http://testapi.imxkj.com//music/fcfed7cb7305840d6bcddc71ba626ee9.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":394297,"playtime":"00:01","bitrate":"2173","height":1088,"width":1920,"fps":25,"md5":"b8a19241332ec97908e9eeac8638b50c","link":"http://testapi.imxkj.com//video/b8a19241332ec97908e9eeac8638b50c.mp4?log_at=3"},"collection_text":"1"},{"id":9582,"title":"测试上传MV","counts":631,"music_type":0,"uid":57985,"nickname":"皮大锤","imgpic":"a3d4d13cbc7b6ed59b10333f143d25a6","category":6,"playtime":"00:00","isdown":0,"video":"83a3916cf107abdf8b259b9a5340bba2","mv":"c99677d04a5ce1f984117c3308b16428","is_collection":0,"song_id":"","collection":0,"counts_text":"631","imgpic_link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"83101","is_long":"0","md5":"a3d4d13cbc7b6ed59b10333f143d25a6","link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3"},"video_link":"http://testapi.imxkj.com//music/83a3916cf107abdf8b259b9a5340bba2.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"83a3916cf107abdf8b259b9a5340bba2","link":"http://testapi.imxkj.com//music/83a3916cf107abdf8b259b9a5340bba2.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":81346481,"playtime":"05:05","bitrate":"2129","height":720,"width":1280,"fps":23,"md5":"c99677d04a5ce1f984117c3308b16428","link":"http://testapi.imxkj.com//video/c99677d04a5ce1f984117c3308b16428.mp4?log_at=3"},"collection_text":"0"},{"id":9550,"title":"测试关联歌曲用户","counts":165,"music_type":0,"uid":81730,"nickname":"钢铁虾X0","imgpic":"eb242fb699614d705a01751a7b5b2d33","category":20,"playtime":"00:00","isdown":0,"video":"B7875D0983831A9C212349006FDBE37B","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"165","imgpic_link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"127792","is_long":"0","md5":"eb242fb699614d705a01751a7b5b2d33","link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3"},"video_link":"http://testapi.imxkj.com//music/B7875D0983831A9C212349006FDBE37B.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"B7875D0983831A9C212349006FDBE37B","link":"http://testapi.imxkj.com//music/B7875D0983831A9C212349006FDBE37B.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9548,"title":"测试协商","counts":1189,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"a838271a008cf2d8e1e53f59355789a8","category":19,"playtime":"00:00","isdown":1,"video":"fb358218dae8b8f83ecae411ff81bc64","mv":"","is_collection":1,"song_id":"","collection":1,"counts_text":"1189","imgpic_link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"157041","is_long":"0","md5":"a838271a008cf2d8e1e53f59355789a8","link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3"},"video_link":"http://testapi.imxkj.com//music/fb358218dae8b8f83ecae411ff81bc64.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"fb358218dae8b8f83ecae411ff81bc64","link":"http://testapi.imxkj.com//music/fb358218dae8b8f83ecae411ff81bc64.mp3?log_at=3"},"mv_info":{},"collection_text":"1"},{"id":9546,"title":"测试关联歌曲用户","counts":339,"music_type":0,"uid":81730,"nickname":"钢铁虾X0","imgpic":"eb242fb699614d705a01751a7b5b2d33","category":19,"playtime":"00:00","isdown":1,"video":"D2CABC891D608FE3EA2C39F4CA670439","mv":"","is_collection":1,"song_id":"","collection":1,"counts_text":"339","imgpic_link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"127792","is_long":"0","md5":"eb242fb699614d705a01751a7b5b2d33","link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3"},"video_link":"http://testapi.imxkj.com//music/D2CABC891D608FE3EA2C39F4CA670439.mp3?log_at=3","video_info":{"ext":"wav","size":"17272910","playtime":"01:29","bitrate":"1536","md5":"D2CABC891D608FE3EA2C39F4CA670439","link":"http://testapi.imxkj.com//music/D2CABC891D608FE3EA2C39F4CA670439.mp3?log_at=3"},"mv_info":{},"collection_text":"1"},{"id":9529,"title":"测试是否有标签","counts":173,"music_type":0,"uid":81731,"nickname":"小豆子","imgpic":"e478d3a43e6098f7c2c38a6ab8ba955d","category":21,"playtime":"00:00","isdown":1,"video":"4688E038BEA0E6FC2F2711020E03BE2A","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"173","imgpic_link":"http://testapi.imxkj.com//image/e478d3a43e6098f7c2c38a6ab8ba955d/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"138124","is_long":"0","md5":"e478d3a43e6098f7c2c38a6ab8ba955d","link":"http://testapi.imxkj.com//image/e478d3a43e6098f7c2c38a6ab8ba955d/3"},"video_link":"http://testapi.imxkj.com//music/4688E038BEA0E6FC2F2711020E03BE2A.mp3?log_at=3","video_info":{"ext":"mp3","size":"11328848","playtime":"04:43","bitrate":"320","md5":"4688E038BEA0E6FC2F2711020E03BE2A","link":"http://testapi.imxkj.com//music/4688E038BEA0E6FC2F2711020E03BE2A.mp3?log_at=3"},"mv_info":{},"collection_text":"0"}],"seo":{"keywords":"测试","title":"测试02|测试1|测试01|测试|测试播放|测试55|测试444|测试333|测试2222|测试111|测试|测试|测试|测试005|测试003|测试上传MV|测试关联歌曲用户|测试协商|测试关联歌曲用户|测试是否有标签 - 源音塘","description":"源音塘是全新的以二次元音乐为主的音乐社区。这里有让耳朵怀孕的丰富良曲、极富魅力的音乐人和偶尔破次元的音乐同好。每天,故事和音乐都在这里"}}
     */

    private int code;
    private String msg;
    private DataBeanX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * count : {"musicCount":30,"musicianCount":2,"songCount":3,"topicCount":36,"topicCount_text":"36"}
         * data : [{"id":9640,"title":"测试02","counts":16,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"7F15A0950ED85D1A102A4C9DC1D92C5B","category":6,"playtime":"00:00","isdown":1,"video":"0138675cb6fc18342c9a55c2d89d2bda","mv":"5DAA4CCF59B2E19560798FD4BCAE0473","is_collection":0,"song_id":"","collection":0,"counts_text":"16","imgpic_link":"http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3","imgpic_info":{"ext":"jpg","w":"3120","h":"4160","size":"5256330","is_long":"0","md5":"7F15A0950ED85D1A102A4C9DC1D92C5B","link":"http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3"},"video_link":"http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3","video_info":{"ext":"mp3","size":"4030424","playtime":"04:11","bitrate":"128","md5":"0138675cb6fc18342c9a55c2d89d2bda","link":"http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":92323169,"playtime":"00:43","bitrate":"17130","height":1088,"width":1920,"fps":30,"md5":"5DAA4CCF59B2E19560798FD4BCAE0473","link":"http://testapi.imxkj.com//video/5DAA4CCF59B2E19560798FD4BCAE0473.mp4?log_at=3"},"collection_text":"0"},{"id":9639,"title":"测试1","counts":16,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"99a5d4941a0c9bb5d02a4153ca191f4a","category":6,"playtime":"00:00","isdown":1,"video":"89fc0c3a28e6ac8e12f1818c435f92c6","mv":"C114858627F589CF430F84EF8DD0D481","is_collection":0,"song_id":"","collection":0,"counts_text":"16","imgpic_link":"http://testapi.imxkj.com//image/99a5d4941a0c9bb5d02a4153ca191f4a/3","imgpic_info":{"ext":"jpg","w":"584","h":"823","size":"65990","is_long":"0","md5":"99a5d4941a0c9bb5d02a4153ca191f4a","link":"http://testapi.imxkj.com//image/99a5d4941a0c9bb5d02a4153ca191f4a/3"},"video_link":"http://testapi.imxkj.com//music/89fc0c3a28e6ac8e12f1818c435f92c6.mp3?log_at=3","video_info":{"ext":"mp3","size":"3407341","playtime":"03:32","bitrate":"128","md5":"89fc0c3a28e6ac8e12f1818c435f92c6","link":"http://testapi.imxkj.com//music/89fc0c3a28e6ac8e12f1818c435f92c6.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":53353112,"playtime":"00:25","bitrate":"17056","height":1088,"width":1920,"fps":30,"md5":"C114858627F589CF430F84EF8DD0D481","link":"http://testapi.imxkj.com//video/C114858627F589CF430F84EF8DD0D481.mp4?log_at=3"},"collection_text":"0"},{"id":9629,"title":"测试01","counts":41,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"8271b42bf3fbba1d3c90a00d56793aba","category":20,"playtime":"00:00","isdown":1,"video":"a4a22a4a848c63338f1d234b2c1ccf6c","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"41","imgpic_link":"http://testapi.imxkj.com//image/8271b42bf3fbba1d3c90a00d56793aba/3","imgpic_info":{"ext":"png","w":"100","h":"100","size":"1641","is_long":"0","md5":"8271b42bf3fbba1d3c90a00d56793aba","link":"http://testapi.imxkj.com//image/8271b42bf3fbba1d3c90a00d56793aba/3"},"video_link":"http://testapi.imxkj.com//music/a4a22a4a848c63338f1d234b2c1ccf6c.mp3?log_at=3","video_info":{"ext":"mp3","size":"3563613","playtime":"03:42","bitrate":"128","md5":"a4a22a4a848c63338f1d234b2c1ccf6c","link":"http://testapi.imxkj.com//music/a4a22a4a848c63338f1d234b2c1ccf6c.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9628,"title":"测试","counts":6,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"2DDDF588128FA22F90AF8B3069685C54","category":19,"playtime":"00:00","isdown":1,"video":"f2cc9ce859a6a982a4772bf4b328438f","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"6","imgpic_link":"http://testapi.imxkj.com//image/2DDDF588128FA22F90AF8B3069685C54/3","imgpic_info":{"ext":"png","w":"720","h":"1280","size":"112262","is_long":"0","md5":"2DDDF588128FA22F90AF8B3069685C54","link":"http://testapi.imxkj.com//image/2DDDF588128FA22F90AF8B3069685C54/3"},"video_link":"http://testapi.imxkj.com//music/f2cc9ce859a6a982a4772bf4b328438f.mp3?log_at=3","video_info":{"ext":"mp3","size":"3197530","playtime":"03:19","bitrate":"128","md5":"f2cc9ce859a6a982a4772bf4b328438f","link":"http://testapi.imxkj.com//music/f2cc9ce859a6a982a4772bf4b328438f.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9627,"title":"测试播放","counts":10,"music_type":0,"uid":57985,"nickname":"皮大锤","imgpic":"E0F4C9AAE7131472BC28406810F558BE","category":6,"playtime":"00:00","isdown":1,"video":"1F796FCBA9867551E18521AD3EFD2989","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"10","imgpic_link":"http://testapi.imxkj.com//image/E0F4C9AAE7131472BC28406810F558BE/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"79452","is_long":"0","md5":"E0F4C9AAE7131472BC28406810F558BE","link":"http://testapi.imxkj.com//image/E0F4C9AAE7131472BC28406810F558BE/3"},"video_link":"http://testapi.imxkj.com//music/1F796FCBA9867551E18521AD3EFD2989.mp3?log_at=3","video_info":{"ext":"mp3","size":"4832631","playtime":"05:01","bitrate":"128","md5":"1F796FCBA9867551E18521AD3EFD2989","link":"http://testapi.imxkj.com//music/1F796FCBA9867551E18521AD3EFD2989.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9624,"title":"测试55","counts":9,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"3E099C51ECD50030D7F202CDD830BB59","category":5,"playtime":"00:00","isdown":1,"video":"9a00fa24dac44291df6a5fc13bf217f2","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"9","imgpic_link":"http://testapi.imxkj.com//image/3E099C51ECD50030D7F202CDD830BB59/3","imgpic_info":{"ext":"jpg","w":"2362","h":"3544","size":"3778129","is_long":"0","md5":"3E099C51ECD50030D7F202CDD830BB59","link":"http://testapi.imxkj.com//image/3E099C51ECD50030D7F202CDD830BB59/3"},"video_link":"http://testapi.imxkj.com//music/9a00fa24dac44291df6a5fc13bf217f2.mp3?log_at=3","video_info":{"ext":"mp3","size":"7864380","playtime":"03:15","bitrate":"321","md5":"9a00fa24dac44291df6a5fc13bf217f2","link":"http://testapi.imxkj.com//music/9a00fa24dac44291df6a5fc13bf217f2.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9623,"title":"测试444","counts":52,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"59b6e18e579f31da685e116f68e2f690","category":6,"playtime":"00:00","isdown":1,"video":"b313b3c52494c4ef2083c0be74181b3d","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"52","imgpic_link":"http://testapi.imxkj.com//image/59b6e18e579f31da685e116f68e2f690/3","imgpic_info":{"ext":"jpg","w":"600","h":"600","size":"44304","is_long":"0","md5":"59b6e18e579f31da685e116f68e2f690","link":"http://testapi.imxkj.com//image/59b6e18e579f31da685e116f68e2f690/3"},"video_link":"http://testapi.imxkj.com//music/b313b3c52494c4ef2083c0be74181b3d.mp3?log_at=3","video_info":{"ext":"mp3","size":"8883692","playtime":"03:41","bitrate":"321","md5":"b313b3c52494c4ef2083c0be74181b3d","link":"http://testapi.imxkj.com//music/b313b3c52494c4ef2083c0be74181b3d.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9622,"title":"测试333","counts":18,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"E61CE4B4B1CF580B15E70340849A3A64","category":5,"playtime":"00:00","isdown":1,"video":"7f9b936a650ef04029d217f473758df4","mv":"2403B6BFAA84C14DF3620F4FCFDC99CF","is_collection":0,"song_id":"","collection":0,"counts_text":"18","imgpic_link":"http://testapi.imxkj.com//image/E61CE4B4B1CF580B15E70340849A3A64/3","imgpic_info":{"ext":"jpg","w":"1279","h":"1226","size":"119141","is_long":"0","md5":"E61CE4B4B1CF580B15E70340849A3A64","link":"http://testapi.imxkj.com//image/E61CE4B4B1CF580B15E70340849A3A64/3"},"video_link":"http://testapi.imxkj.com//music/7f9b936a650ef04029d217f473758df4.mp3?log_at=3","video_info":{"ext":"mp3","size":"5643007","playtime":"03:55","bitrate":"192","md5":"7f9b936a650ef04029d217f473758df4","link":"http://testapi.imxkj.com//music/7f9b936a650ef04029d217f473758df4.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":6808381,"playtime":"00:03","bitrate":"16580","height":1088,"width":1920,"fps":30,"md5":"2403B6BFAA84C14DF3620F4FCFDC99CF","link":"http://testapi.imxkj.com//video/2403B6BFAA84C14DF3620F4FCFDC99CF.mp4?log_at=3"},"collection_text":"0"},{"id":9621,"title":"测试2222","counts":25,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"F2018C5352C1B0818F5195B8B808666A","category":19,"playtime":"00:00","isdown":1,"video":"ef8afe9d6014eac6c0365a1300e1097d","mv":"31053A65A46C0D4A94CDF3BB247413B5","is_collection":1,"song_id":"","collection":1,"counts_text":"25","imgpic_link":"http://testapi.imxkj.com//image/F2018C5352C1B0818F5195B8B808666A/3","imgpic_info":{"ext":"jpg","w":"1000","h":"1000","size":"86123","is_long":"0","md5":"F2018C5352C1B0818F5195B8B808666A","link":"http://testapi.imxkj.com//image/F2018C5352C1B0818F5195B8B808666A/3"},"video_link":"http://testapi.imxkj.com//music/ef8afe9d6014eac6c0365a1300e1097d.mp3?log_at=3","video_info":{"ext":"mp3","size":"10101371","playtime":"04:08","bitrate":"325","md5":"ef8afe9d6014eac6c0365a1300e1097d","link":"http://testapi.imxkj.com//music/ef8afe9d6014eac6c0365a1300e1097d.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":1190062,"playtime":"00:04","bitrate":"1907","height":480,"width":720,"fps":29,"md5":"31053A65A46C0D4A94CDF3BB247413B5","link":"http://testapi.imxkj.com//video/31053A65A46C0D4A94CDF3BB247413B5.mp4?log_at=3"},"collection_text":"1"},{"id":9620,"title":"测试111","counts":26,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"7c1e24cf00019d940fc4a901311171ab","category":5,"playtime":"00:00","isdown":1,"video":"fc0a406abf73e31f39cddf09146a1e36","mv":"67401DD42C2421E92C126FFA9A13219B","is_collection":0,"song_id":"","collection":0,"counts_text":"26","imgpic_link":"http://testapi.imxkj.com//image/7c1e24cf00019d940fc4a901311171ab/3","imgpic_info":{"ext":"jpg","w":"102","h":"112","size":"15396","is_long":"0","md5":"7c1e24cf00019d940fc4a901311171ab","link":"http://testapi.imxkj.com//image/7c1e24cf00019d940fc4a901311171ab/3"},"video_link":"http://testapi.imxkj.com//music/fc0a406abf73e31f39cddf09146a1e36.mp3?log_at=3","video_info":{"ext":"mp3","size":"5071076","playtime":"02:02","bitrate":"331","md5":"fc0a406abf73e31f39cddf09146a1e36","link":"http://testapi.imxkj.com//music/fc0a406abf73e31f39cddf09146a1e36.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":1195195,"playtime":"00:05","bitrate":"1875","height":480,"width":720,"fps":29,"md5":"67401DD42C2421E92C126FFA9A13219B","link":"http://testapi.imxkj.com//video/67401DD42C2421E92C126FFA9A13219B.mp4?log_at=3"},"collection_text":"0"},{"id":9603,"title":"测试","counts":0,"music_type":1,"uid":81811,"nickname":"源音塘920450","imgpic":"a838271a008cf2d8e1e53f59355789a8","category":5,"playtime":"00:00","isdown":1,"video":"412875847BD1592419DF5060C425A0B3","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"157041","is_long":"0","md5":"a838271a008cf2d8e1e53f59355789a8","link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3"},"video_link":"http://testapi.imxkj.com//music/412875847BD1592419DF5060C425A0B3.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"412875847BD1592419DF5060C425A0B3","link":"http://testapi.imxkj.com//music/412875847BD1592419DF5060C425A0B3.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9602,"title":"测试","counts":0,"music_type":0,"uid":81837,"nickname":"源音塘752566","imgpic":"0c78432405e68548035f802d8718a470","category":5,"playtime":"00:00","isdown":1,"video":"DDFFB721A31FF1838A6826BE8E1466E8","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/0c78432405e68548035f802d8718a470/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"58230","is_long":"0","md5":"0c78432405e68548035f802d8718a470","link":"http://testapi.imxkj.com//image/0c78432405e68548035f802d8718a470/3"},"video_link":"http://testapi.imxkj.com//music/DDFFB721A31FF1838A6826BE8E1466E8.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"DDFFB721A31FF1838A6826BE8E1466E8","link":"http://testapi.imxkj.com//music/DDFFB721A31FF1838A6826BE8E1466E8.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9601,"title":"测试","counts":0,"music_type":1,"uid":81735,"nickname":"钢铁侠3","imgpic":"a3d4d13cbc7b6ed59b10333f143d25a6","category":6,"playtime":"00:00","isdown":1,"video":"64D2E7AADC66D454C624B96477714B21","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"0","imgpic_link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"83101","is_long":"0","md5":"a3d4d13cbc7b6ed59b10333f143d25a6","link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3"},"video_link":"http://testapi.imxkj.com//music/64D2E7AADC66D454C624B96477714B21.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"64D2E7AADC66D454C624B96477714B21","link":"http://testapi.imxkj.com//music/64D2E7AADC66D454C624B96477714B21.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9600,"title":"测试005","counts":38,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"e57bdfd6eb951ac2f37091dae0cb2fc1","category":5,"playtime":"00:00","isdown":1,"video":"b59a93a69427f0874164d5bea6e547fd","mv":"3f423ca6736007d7fdce244ff0a78da0","is_collection":1,"song_id":"","collection":1,"counts_text":"38","imgpic_link":"http://testapi.imxkj.com//image/e57bdfd6eb951ac2f37091dae0cb2fc1/3","imgpic_info":{"ext":"png","w":"720","h":"1280","size":"1516613","is_long":"0","md5":"e57bdfd6eb951ac2f37091dae0cb2fc1","link":"http://testapi.imxkj.com//image/e57bdfd6eb951ac2f37091dae0cb2fc1/3"},"video_link":"http://testapi.imxkj.com//music/b59a93a69427f0874164d5bea6e547fd.mp3?log_at=3","video_info":{"ext":"mp3","size":"6089432","playtime":"02:07","bitrate":"383","md5":"b59a93a69427f0874164d5bea6e547fd","link":"http://testapi.imxkj.com//music/b59a93a69427f0874164d5bea6e547fd.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":4817470,"playtime":"00:03","bitrate":"12123","height":1088,"width":1920,"fps":16,"md5":"3f423ca6736007d7fdce244ff0a78da0","link":"http://testapi.imxkj.com//video/3f423ca6736007d7fdce244ff0a78da0.mp4?log_at=3"},"collection_text":"1"},{"id":9597,"title":"测试003","counts":129,"music_type":1,"uid":81731,"nickname":"小豆子","imgpic":"6091d203f6d1dd28fe4076ccfdcd2c35","category":5,"playtime":"00:00","isdown":1,"video":"fcfed7cb7305840d6bcddc71ba626ee9","mv":"b8a19241332ec97908e9eeac8638b50c","is_collection":1,"song_id":"","collection":1,"counts_text":"129","imgpic_link":"http://testapi.imxkj.com//image/6091d203f6d1dd28fe4076ccfdcd2c35/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1920","size":"336739","is_long":"0","md5":"6091d203f6d1dd28fe4076ccfdcd2c35","link":"http://testapi.imxkj.com//image/6091d203f6d1dd28fe4076ccfdcd2c35/3"},"video_link":"http://testapi.imxkj.com//music/fcfed7cb7305840d6bcddc71ba626ee9.mp3?log_at=3","video_info":{"ext":"mp3","size":"5576714","playtime":"05:28","bitrate":"135","md5":"fcfed7cb7305840d6bcddc71ba626ee9","link":"http://testapi.imxkj.com//music/fcfed7cb7305840d6bcddc71ba626ee9.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":394297,"playtime":"00:01","bitrate":"2173","height":1088,"width":1920,"fps":25,"md5":"b8a19241332ec97908e9eeac8638b50c","link":"http://testapi.imxkj.com//video/b8a19241332ec97908e9eeac8638b50c.mp4?log_at=3"},"collection_text":"1"},{"id":9582,"title":"测试上传MV","counts":631,"music_type":0,"uid":57985,"nickname":"皮大锤","imgpic":"a3d4d13cbc7b6ed59b10333f143d25a6","category":6,"playtime":"00:00","isdown":0,"video":"83a3916cf107abdf8b259b9a5340bba2","mv":"c99677d04a5ce1f984117c3308b16428","is_collection":0,"song_id":"","collection":0,"counts_text":"631","imgpic_link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"83101","is_long":"0","md5":"a3d4d13cbc7b6ed59b10333f143d25a6","link":"http://testapi.imxkj.com//image/a3d4d13cbc7b6ed59b10333f143d25a6/3"},"video_link":"http://testapi.imxkj.com//music/83a3916cf107abdf8b259b9a5340bba2.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"83a3916cf107abdf8b259b9a5340bba2","link":"http://testapi.imxkj.com//music/83a3916cf107abdf8b259b9a5340bba2.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":81346481,"playtime":"05:05","bitrate":"2129","height":720,"width":1280,"fps":23,"md5":"c99677d04a5ce1f984117c3308b16428","link":"http://testapi.imxkj.com//video/c99677d04a5ce1f984117c3308b16428.mp4?log_at=3"},"collection_text":"0"},{"id":9550,"title":"测试关联歌曲用户","counts":165,"music_type":0,"uid":81730,"nickname":"钢铁虾X0","imgpic":"eb242fb699614d705a01751a7b5b2d33","category":20,"playtime":"00:00","isdown":0,"video":"B7875D0983831A9C212349006FDBE37B","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"165","imgpic_link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"127792","is_long":"0","md5":"eb242fb699614d705a01751a7b5b2d33","link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3"},"video_link":"http://testapi.imxkj.com//music/B7875D0983831A9C212349006FDBE37B.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"B7875D0983831A9C212349006FDBE37B","link":"http://testapi.imxkj.com//music/B7875D0983831A9C212349006FDBE37B.mp3?log_at=3"},"mv_info":{},"collection_text":"0"},{"id":9548,"title":"测试协商","counts":1189,"music_type":1,"uid":81730,"nickname":"钢铁虾X0","imgpic":"a838271a008cf2d8e1e53f59355789a8","category":19,"playtime":"00:00","isdown":1,"video":"fb358218dae8b8f83ecae411ff81bc64","mv":"","is_collection":1,"song_id":"","collection":1,"counts_text":"1189","imgpic_link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"157041","is_long":"0","md5":"a838271a008cf2d8e1e53f59355789a8","link":"http://testapi.imxkj.com//image/a838271a008cf2d8e1e53f59355789a8/3"},"video_link":"http://testapi.imxkj.com//music/fb358218dae8b8f83ecae411ff81bc64.mp3?log_at=3","video_info":{"ext":"mp3","size":"3399250","playtime":"03:28","bitrate":"130","md5":"fb358218dae8b8f83ecae411ff81bc64","link":"http://testapi.imxkj.com//music/fb358218dae8b8f83ecae411ff81bc64.mp3?log_at=3"},"mv_info":{},"collection_text":"1"},{"id":9546,"title":"测试关联歌曲用户","counts":339,"music_type":0,"uid":81730,"nickname":"钢铁虾X0","imgpic":"eb242fb699614d705a01751a7b5b2d33","category":19,"playtime":"00:00","isdown":1,"video":"D2CABC891D608FE3EA2C39F4CA670439","mv":"","is_collection":1,"song_id":"","collection":1,"counts_text":"339","imgpic_link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"127792","is_long":"0","md5":"eb242fb699614d705a01751a7b5b2d33","link":"http://testapi.imxkj.com//image/eb242fb699614d705a01751a7b5b2d33/3"},"video_link":"http://testapi.imxkj.com//music/D2CABC891D608FE3EA2C39F4CA670439.mp3?log_at=3","video_info":{"ext":"wav","size":"17272910","playtime":"01:29","bitrate":"1536","md5":"D2CABC891D608FE3EA2C39F4CA670439","link":"http://testapi.imxkj.com//music/D2CABC891D608FE3EA2C39F4CA670439.mp3?log_at=3"},"mv_info":{},"collection_text":"1"},{"id":9529,"title":"测试是否有标签","counts":173,"music_type":0,"uid":81731,"nickname":"小豆子","imgpic":"e478d3a43e6098f7c2c38a6ab8ba955d","category":21,"playtime":"00:00","isdown":1,"video":"4688E038BEA0E6FC2F2711020E03BE2A","mv":"","is_collection":0,"song_id":"","collection":0,"counts_text":"173","imgpic_link":"http://testapi.imxkj.com//image/e478d3a43e6098f7c2c38a6ab8ba955d/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"138124","is_long":"0","md5":"e478d3a43e6098f7c2c38a6ab8ba955d","link":"http://testapi.imxkj.com//image/e478d3a43e6098f7c2c38a6ab8ba955d/3"},"video_link":"http://testapi.imxkj.com//music/4688E038BEA0E6FC2F2711020E03BE2A.mp3?log_at=3","video_info":{"ext":"mp3","size":"11328848","playtime":"04:43","bitrate":"320","md5":"4688E038BEA0E6FC2F2711020E03BE2A","link":"http://testapi.imxkj.com//music/4688E038BEA0E6FC2F2711020E03BE2A.mp3?log_at=3"},"mv_info":{},"collection_text":"0"}]
         * seo : {"keywords":"测试","title":"测试02|测试1|测试01|测试|测试播放|测试55|测试444|测试333|测试2222|测试111|测试|测试|测试|测试005|测试003|测试上传MV|测试关联歌曲用户|测试协商|测试关联歌曲用户|测试是否有标签 - 源音塘","description":"源音塘是全新的以二次元音乐为主的音乐社区。这里有让耳朵怀孕的丰富良曲、极富魅力的音乐人和偶尔破次元的音乐同好。每天,故事和音乐都在这里"}
         */

        private CountBean count;
        private SeoBean seo;
        private List<DataBean> data;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public SeoBean getSeo() {
            return seo;
        }

        public void setSeo(SeoBean seo) {
            this.seo = seo;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class CountBean {
            /**
             * musicCount : 30
             * musicianCount : 2
             * songCount : 3
             * topicCount : 36
             * topicCount_text : 36
             */

            private int musicCount;
            private int musicianCount;
            private int songCount;
            private int topicCount;
            private String topicCount_text;

            public int getMusicCount() {
                return musicCount;
            }

            public void setMusicCount(int musicCount) {
                this.musicCount = musicCount;
            }

            public int getMusicianCount() {
                return musicianCount;
            }

            public void setMusicianCount(int musicianCount) {
                this.musicianCount = musicianCount;
            }

            public int getSongCount() {
                return songCount;
            }

            public void setSongCount(int songCount) {
                this.songCount = songCount;
            }

            public int getTopicCount() {
                return topicCount;
            }

            public void setTopicCount(int topicCount) {
                this.topicCount = topicCount;
            }

            public String getTopicCount_text() {
                return topicCount_text;
            }

            public void setTopicCount_text(String topicCount_text) {
                this.topicCount_text = topicCount_text;
            }
        }

        public static class SeoBean {
            /**
             * keywords : 测试
             * title : 测试02|测试1|测试01|测试|测试播放|测试55|测试444|测试333|测试2222|测试111|测试|测试|测试|测试005|测试003|测试上传MV|测试关联歌曲用户|测试协商|测试关联歌曲用户|测试是否有标签 - 源音塘
             * description : 源音塘是全新的以二次元音乐为主的音乐社区。这里有让耳朵怀孕的丰富良曲、极富魅力的音乐人和偶尔破次元的音乐同好。每天,故事和音乐都在这里
             */

            private String keywords;
            private String title;
            private String description;

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class DataBean implements MultiItemEntity {
            /**
             * id : 9640
             * title : 测试02
             * counts : 16
             * music_type : 1
             * uid : 81731
             * nickname : 小豆子
             * imgpic : 7F15A0950ED85D1A102A4C9DC1D92C5B
             * category : 6
             * playtime : 00:00
             * isdown : 1
             * video : 0138675cb6fc18342c9a55c2d89d2bda
             * mv : 5DAA4CCF59B2E19560798FD4BCAE0473
             * is_collection : 0
             * song_id :
             * collection : 0
             * counts_text : 16
             * imgpic_link : http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3
             * imgpic_info : {"ext":"jpg","w":"3120","h":"4160","size":"5256330","is_long":"0","md5":"7F15A0950ED85D1A102A4C9DC1D92C5B","link":"http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3"}
             * video_link : http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3
             * video_info : {"ext":"mp3","size":"4030424","playtime":"04:11","bitrate":"128","md5":"0138675cb6fc18342c9a55c2d89d2bda","link":"http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3"}
             * mv_info : {"ext":"m4a","size":92323169,"playtime":"00:43","bitrate":"17130","height":1088,"width":1920,"fps":30,"md5":"5DAA4CCF59B2E19560798FD4BCAE0473","link":"http://testapi.imxkj.com//video/5DAA4CCF59B2E19560798FD4BCAE0473.mp4?log_at=3"}
             * collection_text : 0
             */

            private int id;
            private String title;
            private int counts;
            private int music_type;
            private int uid;
            private String nickname;
            private String imgpic;
            private int category;
            private String playtime;
            private int isdown;
            private String video;
            private String mv;
            private int is_collection;
            private String song_id;
            private int collection;
            private String counts_text;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;
            private String video_link;
            private VideoInfoBean video_info;
            private MvInfoBean mv_info;
            private String collection_text;

            private int comment;
            private String comment_text;

            public int getComment() {
                return comment;
            }

            public void setComment(int comment) {
                this.comment = comment;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
            }

            public int getMusic_type() {
                return music_type;
            }

            public void setMusic_type(int music_type) {
                this.music_type = music_type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public int getIsdown() {
                return isdown;
            }

            public void setIsdown(int isdown) {
                this.isdown = isdown;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getMv() {
                return mv;
            }

            public void setMv(String mv) {
                this.mv = mv;
            }

            public int getIs_collection() {
                return is_collection;
            }

            public void setIs_collection(int is_collection) {
                this.is_collection = is_collection;
            }

            public String getSong_id() {
                return song_id;
            }

            public void setSong_id(String song_id) {
                this.song_id = song_id;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

            public String getCounts_text() {
                return counts_text;
            }

            public void setCounts_text(String counts_text) {
                this.counts_text = counts_text;
            }

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public String getVideo_link() {
                return video_link;
            }

            public void setVideo_link(String video_link) {
                this.video_link = video_link;
            }

            public VideoInfoBean getVideo_info() {
                return video_info;
            }

            public void setVideo_info(VideoInfoBean video_info) {
                this.video_info = video_info;
            }

            public MvInfoBean getMv_info() {
                return mv_info;
            }

            public void setMv_info(MvInfoBean mv_info) {
                this.mv_info = mv_info;
            }

            public String getCollection_text() {
                return collection_text;
            }

            public void setCollection_text(String collection_text) {
                this.collection_text = collection_text;
            }

            @Override
            public int getItemType() {
                return 0;
            }

            public static class ImgpicInfoBean {
                /**
                 * ext : jpg
                 * w : 3120
                 * h : 4160
                 * size : 5256330
                 * is_long : 0
                 * md5 : 7F15A0950ED85D1A102A4C9DC1D92C5B
                 * link : http://testapi.imxkj.com//image/7F15A0950ED85D1A102A4C9DC1D92C5B/3
                 */

                private String ext;
                private String w;
                private String h;
                private String size;
                private String is_long;
                private String md5;
                private String link;

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public String getW() {
                    return w;
                }

                public void setW(String w) {
                    this.w = w;
                }

                public String getH() {
                    return h;
                }

                public void setH(String h) {
                    this.h = h;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getIs_long() {
                    return is_long;
                }

                public void setIs_long(String is_long) {
                    this.is_long = is_long;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }

            public static class VideoInfoBean {
                /**
                 * ext : mp3
                 * size : 4030424
                 * playtime : 04:11
                 * bitrate : 128
                 * md5 : 0138675cb6fc18342c9a55c2d89d2bda
                 * link : http://testapi.imxkj.com//music/0138675cb6fc18342c9a55c2d89d2bda.mp3?log_at=3
                 */

                private String ext;
                private String size;
                private String playtime;
                private String bitrate;
                private String md5;
                private String link;

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public String getBitrate() {
                    return bitrate;
                }

                public void setBitrate(String bitrate) {
                    this.bitrate = bitrate;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }

            public static class MvInfoBean {
                /**
                 * ext : m4a
                 * size : 92323169
                 * playtime : 00:43
                 * bitrate : 17130
                 * height : 1088
                 * width : 1920
                 * fps : 30
                 * md5 : 5DAA4CCF59B2E19560798FD4BCAE0473
                 * link : http://testapi.imxkj.com//video/5DAA4CCF59B2E19560798FD4BCAE0473.mp4?log_at=3
                 */

                private String ext;
                private int size;
                private String playtime;
                private String bitrate;
                private int height;
                private int width;
                private int fps;
                private String md5;
                private String link;

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public String getBitrate() {
                    return bitrate;
                }

                public void setBitrate(String bitrate) {
                    this.bitrate = bitrate;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getFps() {
                    return fps;
                }

                public void setFps(int fps) {
                    this.fps = fps;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }
        }
    }
}
