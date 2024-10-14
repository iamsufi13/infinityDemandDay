-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: contenttree
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banner_entity`
--

DROP TABLE IF EXISTS `banner_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `banner_type` varchar(255) DEFAULT NULL,
  `dt1` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `file_path` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner_entity`
--

LOCK TABLES `banner_entity` WRITE;
/*!40000 ALTER TABLE `banner_entity` DISABLE KEYS */;
INSERT INTO `banner_entity` VALUES (1,'application/octet-stream','2024-10-01 21:16:45.044482','download.jfif',_binary 'ÿ\Øÿ\à\0JFIF\0\0\0\0\0\0ÿ\Û\0„\0	( \Z%!1!%)+...383-7(-.+\n\n\n\r-%%-----/--------------------------------------------ÿÀ\0\0\à\0\á\"\0ÿ\Ä\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿ\Ä\0=\0\0\0\0\0\0!1AQa\"q‘¡±Á\Ñð2BR\áñ#br‚¢²ƒ’\Â\âÿ\Ä\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿ\Ä\0$\0\0\0\0\0\0\0\0\0!1A\"Q2Rÿ\Ú\0\0\0?\0úž««\éN5Ëž=H®q(¹\Ê%\Ël\Ú	\Þ(9\ê„¬\n:\ç¨ñ(8®J\r$J\àzZ¾)£uI˜\ç\ín\ë,®<2›\Ñ{_©TŸh\Z\Ý\Ö[0\Ï\ßP\Ã%/‡\Ê*\Õ2\éºG+\è\íXq\âW‘b³\×\Ô0\É\\\Ã\åU*t­S\ÙÖ²$-+Z,¨þ’\Éå¾ ©Ü·³\àl´x\\[²;X¤™*8Üœ¶\ÉHY\Ô\æ®y4©˜hýN^–\ÙZv‡2\î˜@ýN°úŸOªÆ²´\Û^jY\'\è®87³AØ¬kƒE\ÆAM“$Çµý¹µÎ²÷ð\â)9¿\ÄºÍˆ\"·\ëp»‰™•Ha\ÏA{”Ú¢ðªD\Zô)¨¹\0v!p9/ˆÄ†î”[\èp8!\ÖÅµº•›Ì»B\Öu‘\Ì;L÷ž]+šGn/RÜ´£ÿ\0j³š\âù_\ížk\É~F[þL_\ÙfkQ¥p®CÊ£Ž*¢€¸\àƒHµ\Ê.rlC[º \Í;@\Æ\î²\è¶<2›\Ò.\ëbƒuTy–|\Ön²Y†Q\æ\n)«T\Ë\å#•ôv¬ñ+›\Çg¯¨a’…„\Ê*U2\éZ|»³\Íh¸W\Ø|[ BöF~[ª‚¤g²þ\Î5±e \Ã`\ÚÝ“<*m\n‰Q\Ç)7\ÙÐº¸W\n%)Ln\'†\Ãõq8°\Ý\î©3Q\rsõ{¥\éo½­\ê£<ž‘hcö\Êû\ÞU k,>g\Ô\ÊTTh¿N~\ë\Ô\Ù\ÃaoP}T+I\Þü\ïòS[,˜l\"+R$m¢\0ºú_\nù-\ÚA\âŸHû¯«`k\n”\Øñp\æƒðUÅ«Dóúa˜\Õ\ç)\Ê_\\\rJ±›\è/\\CF¥S\æ\ÛX\r\Ö36\íA&%#’Gv\nR\Û\è\×\æ™\ëX5X¼×´\Îy†IH\áð5±]0µ9Oe@¹	>\Ò:L85³+…\Ë*\Ö2\éZŒ«²\àA!k0yS[VŒÃ¦PH\â\Í\åO\'l\Íÿ\0bK«I\Ý/\'9¬`‹!J#©YüÓ´l`7C-‹\æôh*bZ\Ý\Õk\Ú°…ŒÌ»Nú†)ƒ\æ“\ÃeU«y*n‡jññ\â\ÞGþ\Z|f{O¹\ãt’Ik@±q‰\Õeßˆ¥PË˜ñÿ\0p;\áÂ›\í6\0Ñ¥DE¥þþOI\ãu	I§³!&\ÓqtfF\Ü)°x“ M–\Ï	ƒ`…òR\á\Zm”\çõ¨@ž&n\Óô½“\Ç%vŽ|˜¯iŸOR,Yì·´Ì­úO‹v:\Ç\Ð\èUˆ\ÍFŽ*ü‘!ñ\Èy¡vBª~n\ÑôIVÌ‹¦YK,\ÑC,-öhY£t†\'2f19©\æ¡\ÙŽb3{H\æ¡/\"ô‹G\rmŽVÅ€\î\'%qø°i‹Á3n“*¶¦crØ™ü²R»\ç].|—79n‹ð^\Ä\ê\×xu´\\9“Å ŸNœöN´^ Y\r\Ô\Ú\ák¿šxN»Q\ÃbQ\Ð\î(;Í‡²úGdªð\á«K\Ú}VŽ¡§‚8µ$\ÚEuƒ\Ìh–Ó¹\ë;®ˆM)XŽ<´ú4Yžx\Ö\r@X¬ÛµrHe\ÏD¥|ŸTøÉ¾\ßuq•vK†$*Ü¥\Ñ\Ñ\Ï­³;G_e\Ò\Ó\å=“	Y—\åm`\ÑYµ \'PH\ã\Í\å\Ï\'}x,¥¬\nÁ”ÀD\ã\nI\èå³¬\á\0\"\ä\0I^A•\ÔòLÛµoy-§%)\ÊkW2ò|–£(\ìŸ\r•†h¦¢\ßg£“\Í\×j‘E•öe­B\Ñ\á²ö4hK\nd¨\à”›\ì£\í®[\Þa‰n´\Ïtˆ?EóP\r—\Úû“_)\ívVpµ·\àt¹‡¦\íóe°\ÚgO=8•Îª	€	<—©T¼\Òy\Ç9:*ð\âû7‹y}t\Þ\Z oh\ÇCÉ†8–µ\Þ!\Ô\ËM–\ç,!\Ò#}H\äG1\Ì,MGñx[u<ãª°\Ã0´›n¡7CF< \ÇºK m²R¶e\ÃPx¹‚\"\åT?\"Z	¹\ÇO\Í\îj\\\í{Œ½Dk\ê=”8²ª‹<C<=M\Ú<î¹‚q€KAÀ?\æ\ÒS[\Þx˜As©Àª(´¸	-\"|­¾\Õ4bv‹\ÜN\r‘$pžª­©À\é\ÆöòþˆÙ®&pü`ÿ\0Gø¢þWS\Å\Ð‹^À8ƒdóÐƒò)œo¡®ÁÒ¥u¾ß—C·ó(9/½\ri\Þ\'\È…t)K£aùê³…«6R§Echsm#ê¬²¦ðó	Š˜vð\ØI>\ë¸\'h‹x\Ó•£U‚\rpÒ¬;²øz\Ü&A²\Ò\à±!\áwb\è\ä\É¥¼’8Ç¸p\Êk\Õp ª²,\È;2p:\ës#\nÒ®TËƒ+’¤Ì¡<&4»d\ï“|¸56(„\É1Š\Ùrò²\î\Ç%\äP\ÚB\'•Ú…=F¡”\á\ÉE\årBSh9¨Kþ¡å¯†=Ø—°ñóo\ï\0y\ÅýõjÖ²DbL\ÝJsUE±Á§g\Äh\Õ<­µ@©‹¸kA-3\åm«¶H£Š.h_\Ô4\Çp9J\Ía(Vµ‡ºœgútI^\Ðö™¦\ÐI&‰¦\ÞV]ubo÷ø)Uµ\Ò\èôZ98ó‚?\âA\\ó\Û:ApFòw¶“~G˜è£Œ¨\ßX-\ÈÌ‘õ\ÑhTa0e¤DA4:*\Üûöø­v\ØzŠ:–ËŠŽ8\ï\Ù.¤\ï\Ö7iØ‚C0¬‹\Û\\ 2 g\ÊU®I‹k°á®¼ˆsuœ{ü•kð\Ü#„x\é\0@\è$Ÿ”„\Î\"F[\ØZ8\Î*“ \ï• \É\êr\Z\è õ\ÚGOº\Ê`\Ø\Z_L‹j:F‘Ï”y+\â\Ëu´ˆ¶„\ïù\ÕNÌ’µD»ž\ç\Øýp\íóW¸\Ìe*3R£À\Òy,\Ök‰Šf«ôeú“¤2V_B¡«Aø§ñ\Z\à–°E1\0\Òl]\â·@©¦O$ú³v\Þ\Úa\ák§m\æû(œ\Íõ-D¦ z,~\'\n\Ún¥R›ñ‘§ \ëå¬¯¤\Õhe6¾\0‘§\É$\ãj\Ó\ÐÐ’^¶V\à1\Õ<`À°&\ÒwŽal{3™5\ÄF†Þ«$\ìPb#G}rWºQD‰YŽ\\XMrNÏ¬6 Rk\Ò8g\Êcz\Ä/-¬ŸYGŸY¿Bs”	@P\Ïx¼”\ã^X\\žkÀ\ÂOˆ\Ø,”©\r¶K‰*+gd£\ÝÌ¯\Z«™\Í\È\éX\ÒC®¥B„Ü¡Q9…\ç\ìš1ý2NŒ·nðB h\Ü/bpA¦e\Äé®«\éý£g\ç+˜\Óøb\ÍFzz)\ê™Q‡¤cs\ÐØ„\íZúÅ·$z|\Ñ_L†\é}’·\Ïu\Û\è•v;\èeøM\í}\ì}y¢(\r,>&™\é[ûü5\íw‚“e\Î0\Ù\Ðs:ieYÀUmZµœ\Zê¦\0t\ã©h™€5>Šð\Çdg’»583\é\Ë&A»O\æˆÆ¡\r¯ ¶¿\Ó\â²\î\ÆUÁ\àóRœÃšó\'Í®\Õl)×§‰£\ÞS;OQ\Z‚Ž\Z´b°¡¡c\Ñ\Z¶ˆ½¶\ÕD‘\é	\Ü0D¿•²‡·\Ør2ð\æ\ìöO”ý\áf0y\Å0\n„#^bà¯¡gš\ê¦\ë´\Þ5ˆ;û,÷fû?‡—:¤ºób\ÄL*rƒ\Ù5	$)’Ñ«‹¯O»dP¥%\Ïvä‘Zn\Ðf\Í>À\í\"\ÖQÌ³FðwT\Çm¾•s\Å`\ÒNäŸ§U)It‹B¶Y\åœO10-\ê´9m!16\ß\Ígp\Í\î¡\Ï2\ï\ÝkU\Ö_ñÌ9ô[“\Ù<¾¤a\ÞG’‹\ë]/M\Þ\ä¹y]örP\Û^TÄ¥©\ÔM5\áºãŠ—\Z‰X——eu\0GP\0ªlv „lu_u[ˆ¨`®,“¶v\ã!VdFšòNeüO\ëWFˆ{®¯\\@hhK7¶<\Ý£G\Å\0\Ù=¡/ƒ°$’\Ï\Å8v•Ðˆ1Lõ\Ð=\Ö\Å\Å\ê¶ùïŠ”õº\Ãc‰i½J–E²˜Ø¦u#\Â\ß\Ïe_C//ñTq¾\Úð²\ëuP~róMci›9¾¢QKô\'š\ä„pÕ§2Àt:\ÚöUU14ž\á6<.±‘pG5t\Ì\ÑÌ´\Øû|”_R‹\Ï¨´ž`\Ç\ÉQOô›\Æe3¼ÀTŠlñA\Ô|‚¹\ì~5ôÀðC\\wGm*\ë_\Ãý\Ý´\í6Ô’Ps‚\âhý“<‰GŠ28·l\Ñ\ÑÅ€|ÿ\0?<“\Ô*‰‘¸\é\ç\æV&–-\Í\á\å¬ù\è<\Õþ\Zó\ÕI2Ž#y¨‰:ñl«²–žFúˆ÷ñ™‹	v‘¿\Ìr@\ì\î,8žq\rKšF¤›t0’PwcFIDgƒh<Q\Zý•m)/\ÒyöZ¬>#‰\ä‡\è8@¸\êd¦Û•‘\â†6g\Â9ÎžÉ¡¶Nyi—a\È\ÐOø‡¹²w%ÁŽõ²\é¸ý\"þFÿ\0,YRK\'›ž÷ö¢\ìŽ9ü^ ÿ\0ý•\Ô7G?\'Fº“,ªWÁ]$lZ¦Š¬hG\éB\â•Ò°OU\ä	+\È4^°“*Ÿ5w„‰º¸\ÇW\rY\ì\Û\á$…\Ã5£®,[* \î-U\Å\Zd¾úI‘U$’Ž<Ñ¡§\ØûCz•Q™\â\Z7‚ö;4\à6¿¢¢¬\ç\Õ2x¾A[—¢4YQ¬I\í\ÜnV\ç—Fžk0u\\\×\0\"7¶\ÝJ®Ï¨5Ž\â\Æóü\ÖJ6¬\ØJºsD\Ê+(¹À’iõi¸l\î¬\0€cª^#r±|F\çõDs\Zü’n\Ã@tÞ’>\nÒOñ“\å	š8\0û\É>{-ø\ï ù+²’\\5\ç\Ë\æ­óM•pM¦/¿\æŠbxl1ñ7ý\Ñþ©ºG:Èž\Ä1X1\Â\0Q§\ÙQhwR×‡´m\á$úE¡jE\"\ÒCž	\è,$ó)²G7[\Õlb%:\Íj·®.1\Ä\Ð\âbû\Ç;­\ÎA”·†h>*ñ>\r\ähèª³Žˆ¬Ç‡\Î+³@7€yHê¯Ž8\È:\r†¿5Z\Õ1\'+z¢^Ó«hƒ<‹\É\æd#añt3Tº:RJ¨8›Ù¢ord™N¶£ƒ$»Ÿ\0þRHôŸ$%D¤\ÏRq{­HMœ0=\Ã\Ín2\Êb›\0\0½‚\Ìvrƒ\r@\ác·\ê\Ó1\è}´‚5UÇ½“‘\'Õž_%\ÎòœSŠ¼”&²\ë\Ì+¨\ì<\rTª¸l‘\ï\rB\È^Jw¥y•ujq»T®7\Æ.T(VD¨\ã³ \à´\Ö\Î\Ïz–\á\Z\ÏwO\â/a-•\ãZñ-\Zn‹Ž©°#Tc»\'S ó\æ©ñC»ý\â~JÆ®\"þ‹+\Ú\n®›\é\ìšN–„J\Ý\Åf&`5?\É;‡\"µ\"\×k·?Š\É~\Ö8¶N\á3p\"O‚h»\ì\É*\è3À\â6C§‚i2Lù«\â[WMy\'V€n“=5ÿ\0\çæ³‰¼€3\n€ñó\é\ë	¼+À6%Ç§\ßS\é\î” ‘\ÂßŸ\Ý3‡¸\ÒË˜¸ÿ\0ô\Å\"\Ñ9\"ÖA\Z	\é\Ï\ê}\Ð1/q½Gð´\è\Ö\ë\ç1>\ÈôÙ§\×Aüš.ŠžQ%\Ç\Óoæ«:=N ka¢#÷Ÿ¿¦\èŒ\\\É.´oa\Ö#ˆ™&\æ=,	t&e\ÒeÎ·#\êRSÐ®(º †ƒ\Ã\Ó}a–\\\è\'ˆ\ÛkÛ¤«65 I|N(~|ºH9a°\Ü$›\îSTk\î R.¨H\ÕF›\ÈP”†Q³U•\â%\áÍ±Ýºzùò¥7ZW\Ï;>ù¨\Ð\Zd*\áwlLŠsA¨šÛ£T\n\Ä\Å\ÙM0\Æ.Q§t\é¦\0‹©#6ˆ	ƒL \ÔA§-\ÍyG„ò\\@?\Ìq\ÜÂ¥f\"­g†±¶›•©Ì²ðe%À²˜\ê¼\ä›twòIXþ)S)™W\Ò_\Õ;š¾ nªª0ñ	U„¯\Ù0\â\Þfw*‹>w’U\Æb\ã*Š½2\âo>vC~ŒŠöd\ê°ñX\Ù9H‰q\ìODLKCkŸ€ûúû$\ëµÏ¼é«Žƒ\í\ä-ö[P\Ì\"8<#\â=ú\ì¬[TDº\Çø~ÿ\0eœ\Â\Ö\r0\Ýwq\×ý#añVxk\Ü\è>\'—\ÝU4ÈµElÁ7\'A·™\é\ÑCd\Þ\ç™A²‘$››O!ùd:u/\Ö\ç\Ï\Èk\çZ\Æ\é\Ñ\á¶û\Î\çX=§ª‹¤ñ;Ÿ„z›ü¹K?\à-\ä>þ·÷P¥Q\ä_ø›õAƒ\ÇÁ\'•½’51Ž6\nF“‰2f\ç\âT\éa0mZšq\ëðû.\ÓÃ“©º²§‡\nf‚F›1\n­€”a¾©\ÜH:d9FE j{7O\Ä\í…bû2Óª\Ô2±]D†Wö,©…Ç¥YX©St›«¦—‡Š&¢\r\ßs(\èUñ\ÄT\êX ;Ö¯*\Îùye˜TÕ©\â!{,d¼\ÞÁ\r´È’W0U‹fw\\3¯\ÐlmY¨Yà©¶™’N‰L0.y$Xit{0\î!À¶¼\ÖKY\Íq\0\ë\îµ9`9,®nI˜Ó¦þh‘°¨\æ\êLô\Zú…ü¶@•¦\î®~\Ñ(\Z‚ðx­\ï°Ÿ ¬0\Õô#M\Z7ÿ\01\ëù²J™–ò\â÷\á\ZŸR?\Û\ÕJ…Yw‡Aô\Ù2t+V\\ƒ$4mo3¿\Å„A\ê}o\î^\n¯	q\ä>&\Ã\ç>‰®ø€=Iú*©\"N!\r9F¢\ÍG‘ü÷P ùF¦où\è„\Ìh)¡s\æ¦\Úap\Ô\ÝB¾\".ŸBPB\Ô\ZúY	\Õ\ÌôC5w•91\Ò¨þj,F\êU^U†Qƒ—\0;\Û,´Og0°Ë«jD%#§ˆ¥\Ã\ÕvF4¨\ånÝž\ã2½Q\Æ\ÈTñ\0#5\ÓtP}bŠ\ÇHJ:K¡1LE–°\'A\Ä.¾¤\ÙuµxBTVñ-07vW—x×–[Œ2\è,\Ã\Ë\Ú6\Ý¶ ‚b\åQ\Ü@•\Ê\ÚgR±œy¼†#\Â\0“~K™†7€’nR5\\ú˜ZÙ”C4u ž«^O\ÉZc‰\Òl©q,\é\n%¢¿NöIpÉ\É\0y«.ò\×B\ÅÝ¤f\ßYZ±l[\â@\ÒÀ”i\ëº>¨-XGT \Óh@\Ì#†y»þ\"\ß2‰L\É=[11“G\Æÿ\0Tzx¨Mbqe½2Ž\Ñh•V\ÌU‘i\Õs™‚½„\ï ù Wpl¡Á\Ì\ëù(5(“iZ\ä\ÅQ@\ê\â \ÂAqº#\èÁ@Hí¤O»h¶\ëK\Ù<¿\È,\Å;•¹\ìe8$\Â ¾\Â\Í\è¼v\Â\è˜ãª·{%+R‰\Ùv+ŸDB	Ê˜wrC\î\n\0\å}\ê¸¥Ü†\Ðj5¬ºeÃš…VFˆv\ä’ðy/,°¢‚›Žš&Ó¢kZ£u\Ç0\Ê\äŒN¦\Ì\Þy\Ä\\#EÑŽ€=!1œ˜j\Ï8Îˆ\èÕ´3™T$[ER]\Î\êÊ‰A3\×e_\\€ah­Zs\æ„\àt\ëO\êŠ\ç© j\î§DŒ£6B~HZs„I·\à²-\ZC\â¼\Ü1Ø£2™‹ \ÆÎ¾€Ë´‰%í”ƒP-‡mIW[[ž\Ú%ª”šF\è03\êJz®=À‹\"5«\Âj·Ý™-óXl\r)+e©\Ã6>Å™³¦%ôÀ\n¿‰°Nº°p]D(\0®T¦6Ql®Ò©t\Zp\ÒQý–.˜}@—«‰0´\0Ö‚–xFl•\Ò!`\ájòv¼€((:$/U¯\á=›H\è—\ÄS±j\ä\Ù\ÒSµýóˆq°Uø\Ì\Z#)“º\ÆqXÛ’Qúè¬©_„pTñºg‡ºR®’\Ð ÷7e\â\à\î¹J‘™\Ø#ºŒ¦¡[\ÛóSk¦\ÇtGR„&¼y-£,>±7°S/\Ñ.Ú€Jó|m™Z`Ëª…\ÖK_]‡ºqH÷JEISp”¾®É±üH\Ô\ÛDf\ÔIÔ©0™¢\ÉXÀ´\Ë%]²©\â\n¯ Jf…_F¼£\ÄÛ§\è°W‘?Â­ˆWZ\èƒ=TM‚p[º?xƒ\Ý+L\0\å\æ0Bñ\n,m\Ðš\Øo®\r‘œa(%a¤¬¼½\Ý\äÿ\Ù');
/*!40000 ALTER TABLE `banner_entity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-09 21:50:50
