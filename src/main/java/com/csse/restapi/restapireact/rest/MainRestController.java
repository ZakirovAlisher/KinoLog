package com.csse.restapi.restapireact.rest;

import com.csse.restapi.restapireact.entities.*;
import com.csse.restapi.restapireact.models.*;
import com.csse.restapi.restapireact.repositories.MovieRepository;
import com.csse.restapi.restapireact.repositories.ScheduleRepository;

import com.csse.restapi.restapireact.services.MovieService;
import com.csse.restapi.restapireact.services.SendEmailService;
import com.csse.restapi.restapireact.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api")
public class MainRestController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleRepository scheduleRepository;



    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user = (Users) authentication.getPrincipal();
            return user;
        }
        return null;
    }




    @GetMapping(value = "/profile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> profilePage(){






        Users user = getUser();
        System.out.println (user.getEmail ());
        return new ResponseEntity<>(new UserDTO (user.getId(), user.getEmail(),user.getFullName (), user.getAvatar (), user.getRoles()), HttpStatus.OK);
    }



    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody Users user){
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/updProfile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updProfile(@RequestBody UserDTO userd){
        Users user = userService.getUserByE (userd.getEmail ());
        user.setFullName (userd.getFullName ());
        user.setAvatar (userd.getAvaUrl ());
        userService.saveUser (user);
        return ResponseEntity.ok(userd);
    }

    @Autowired

    PasswordEncoder passwordEncoder;

    @PutMapping("/changePass")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO2 request) {
        Users existedUser = userService.getUserByE (request.getEmail());

        if (passwordEncoder.matches(request.getOld (), existedUser.getPassword())) {
            if (request.getNeww ().equals (request.getRenew ())) {

                existedUser.setPassword (passwordEncoder.encode (request.getNeww ()));
                userService.saveUser (existedUser);
                return ResponseEntity.ok (existedUser);

            }

        }

        return ResponseEntity.badRequest().body("ERROR");
    }


////////////////////////MOVIES CRUD

    @GetMapping(value = "/allmovies/{title}/{page}/{size}")
    public ResponseEntity<Map<String, Object>> getAllMovies(
            @PathVariable(name = "title") String title,
            @PathVariable(name = "page") int page,
            @PathVariable(name = "size") int size

    ){

        try {
            List<Movies> tutorials = new ArrayList<Movies>();
            Pageable paging = PageRequest.of(page, size);

            Page<Movies> pageTuts;
            if (title.equals ("all"))

                pageTuts = movieRepository.findAll (paging);
            else
                pageTuts = movieRepository.findByTitleContaining(title, paging);

            tutorials = pageTuts.getContent();

            Map<String, Object> response = new HashMap<> ();
            response.put("movies", tutorials);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }




//        List<Movies> items = movieService.getAllMovies();
//        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/addmovie")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> addMovie(@RequestBody Movies item){
        movieService.addMovie(item);
        return ResponseEntity.ok(item);
    }

    @PutMapping(value = "/savemovie")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveMovie(@RequestBody Movies item){
        movieService.saveMovie(item);
        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/getmovie/{id}")
    public ResponseEntity<?> getMovie(@PathVariable(name = "id") Long id){
        Movies item = movieService.getMovie(id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/deletemovie")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> deleteMovie(@RequestBody Movies item){
        Movies checkItem = movieService.getMovie(item.getId());
        if(checkItem!=null){
            movieService.deleteMovie(checkItem);
            return ResponseEntity.ok(checkItem);
        }
        return ResponseEntity.ok(item);
    }
    @GetMapping(value = "/allmovies2")
    public ResponseEntity<?> allM(){

        List<Movies> items = movieService.getAllMovies ();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
////////////////////////SCHEDULES CRUD
@GetMapping(value = "/schedules/{search}")
public ResponseEntity<?> getSchedules(@PathVariable(name = "search") String search) throws JsonProcessingException, ParseException {

    List<Schedules> items = movieService.getAllSchedules();
    List<Schedules> itemsSearch = new ArrayList<> ();
    System.out.println (search.getClass ());
    if (!search.equals ("all")){
        for (Schedules sc: items
             ) {
            if(!sc.getMovie ().getTitle ().toLowerCase ().contains (search.toLowerCase ())){
               itemsSearch.add(sc);
            }
        }

        items.removeAll (itemsSearch);

    }



   Map<  Schedules , Date> map = new HashMap<>();
    for (Schedules sc: items
    ) {


        map.put(  sc, sc.getDay ());

    }



    Map<Date, Map<Movies, Map<Integer, List<Schedules>>>> groupedMap = items.stream ()
            .collect(groupingBy(Schedules::getDay, groupingBy(Schedules::getMovie, groupingBy(Schedules::getHall)))

            );

    ArrayList<DayViewModel> sc = new ArrayList<> ();

    for (Map.Entry<Date, Map<Movies, Map<Integer, List<Schedules>>>> s : groupedMap.entrySet ()
         ) {


        SimpleDateFormat month_date = new SimpleDateFormat("d MMMMMMMMMMMMMMMM", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        String month_name = month_date.format(s.getKey ());


        DayViewModel dvm = new DayViewModel ();
        dvm.setDay (s.getKey ());

        SimpleDateFormat sdf22 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateWithoutTime2 = sdf22.parse(sdf22.format(new java.util.Date()));
        if(s.getKey ().equals (dateWithoutTime2)){
            month_name += " (Today)";
        }

        dvm.setDayStr (month_name);
        ArrayList<MovieViewModel> sc2 = new ArrayList<> ();

        for (Map.Entry<Movies, Map<Integer, List<Schedules>>> s2 : s.getValue ().entrySet ()
        ) {

            MovieViewModel mvm = new MovieViewModel ();

            mvm.setMovie (s2.getKey ());
            mvm.setRating (s2.getKey ().getRating ());
            ArrayList<HallViewModel> sc3 = new ArrayList<> ();


            for (Map.Entry<Integer, List<Schedules>> s3 : s2.getValue ().entrySet ()
            ) {
                ArrayList<Schedules> sc4 = new ArrayList<> ();
                sc4 = (ArrayList<Schedules>) s3.getValue ();
                sc4.sort (Comparator.comparing (Schedules::getTime));



                HallViewModel hvm = new HallViewModel ();

                hvm.setHallNumber (s3.getKey ());

                hvm.setSchedules (sc4);


                sc3.add(hvm);



            }
            sc3.sort (Comparator.comparing (HallViewModel::getHallNumber));
            mvm.setHalls (sc3);
            sc2.add (mvm);




        }

        sc2.sort (Comparator.comparing (MovieViewModel::getRating).reversed ());
        dvm.setMovies (sc2);

        sc.add (dvm);


    }

    sc.sort (Comparator.comparing (DayViewModel::getDay));





    GsonBuilder gsonMapBuilder = new GsonBuilder();

    Gson gsonObject = gsonMapBuilder.create();

    String JSONObject = gsonObject.toJson(sc);


    Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    String prettyJson = prettyGson.toJson(sc);



    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date dateWithoutTime = sdf2.parse(sdf2.format(new java.util.Date()));

    Date sqlDatePlus = new Date(dateWithoutTime.getTime()+10*24*60*60*1000);
    Date sqlDateMinus = new Date(dateWithoutTime.getTime()-10*24*60*60*1000);
    Date today = new Date(dateWithoutTime.getTime() - 1*24*60*60*1000);

    ArrayList<DayViewModel> scPlusMinus3 = new ArrayList<> ();
    for (DayViewModel s : sc){
        if(  s.getDay ().after (today)  && s.getDay ().before (sqlDatePlus)  ){
            scPlusMinus3.add (s);
        }

    }



//scPlusMinus3 TODAY + 10 DAYS ACTUAL SCHEDULE


    return new ResponseEntity<>(scPlusMinus3, HttpStatus.OK);
}
    @GetMapping(value = "/allschedules")
    public ResponseEntity<?> getAllSchedules(){

        List<Schedules> items = movieService.getAllSchedules();


        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/addschedule")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleDTO item){
        Schedules sc = new Schedules ();

        sc.setId (item.getId ());
        sc.setDay (item.getDay ());
        sc.setTime (Time.valueOf (item.getTime ()));
        sc.setPrice_ad (item.getPrice_ad ());
        sc.setPrice_st (item.getPrice_st ());
        sc.setPrice_ch (item.getPrice_ch ());



        sc.setFinished (item.isFinished ());
        sc.setMovie (movieService.getMovie (item.getMovie_id ()));


        Halls hh = movieService.getHall ((long) item.getHall ());

        sc.setHall (hh.getNumber ().intValue ());

        sc.setPlaces (hh.getPlaces ());


//        int[][] posts = gsonObject.fromJson(JSONObject, int[][].class);
//
//        System.out.println (Arrays.deepToString (posts));



        movieService.addSchedule(sc);
        return ResponseEntity.ok(item);
    }

    @PutMapping(value = "/saveschedule")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleDTO item){
        Schedules sc = new Schedules ();

        sc.setId (item.getId ());
        sc.setDay (item.getDay ());

        sc.setTime (Time.valueOf (item.getTime ()));
        sc.setPrice_ad (item.getPrice_ad ());
        sc.setPrice_st (item.getPrice_st ());
        sc.setPrice_ch (item.getPrice_ch ());
        sc.setPlaces (item.getPlaces ());
        sc.setHall (item.getHall ());
        sc.setFinished (item.isFinished ());
        sc.setMovie (movieService.getMovie (item.getMovie_id ()));
        movieService.addSchedule(sc);
        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/getschedule/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable(name = "id") Long id){
        Schedules item = movieService.getSchedule(id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value = "/getschedulenotadm/{id}")
    public ResponseEntity<?> getSchedule2(@PathVariable(name = "id") Long id){
        Schedules item = movieService.getSchedule(id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping(value = "/deleteschedule")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> deleteSchedule(@RequestBody Schedules item){
        Schedules checkItem = movieService.getSchedule(item.getId());
        if(checkItem!=null){
            movieService.deleteSchedule (checkItem);
            return ResponseEntity.ok(checkItem);
        }
        return ResponseEntity.ok(item);
    }

////////////////////////USERS CRUD

    @GetMapping(value = "/allusers")
    public ResponseEntity<?> getAllUsers(){

        List<Users> items = userService.AdmGetAllUsers ();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/adduser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> addUser(@RequestBody Users item){

        userService.AdmAddUser(item);
        return ResponseEntity.ok(item);
    }

    @PutMapping(value = "/saveuser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveUser(@RequestBody Users item){

        userService.AdmSaveUser (item);
        return ResponseEntity.ok(item);
    }
    @PutMapping(value = "/saveuserinfo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveUserInfo(@RequestBody Users item){

        userService.AdmSaveUserInfo (item);
        return ResponseEntity.ok(item);
    }
    @GetMapping(value = "/getuser/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") Long id){
        Users item = userService.AdmGetUser (id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/deleteuser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> deleteUser(@RequestBody Schedules item){
        Users checkItem = userService.AdmGetUser(item.getId());
        if(checkItem!=null){
            userService.AdmDeleteUser (checkItem);
            return ResponseEntity.ok(checkItem);
        }
        return ResponseEntity.ok(item);
    }

    //////////////////ROLES

    @GetMapping(value = "/allroles")
    public ResponseEntity<?> getAllRoles(){

        List<Roles> items = userService.getAllRoles ();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/assignrole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> assignrole(@RequestBody AssignRoleDTO item){


        Users user = userService.AdmGetUser (item.getUser_id() );
        List<Roles> roles = user.getRoles ();
        Roles addedRole =  userService.getRole (item.getRole_id ());

        if(roles.contains (addedRole))
        {
           roles.remove (addedRole);
        }
        else {
        roles.add (addedRole);

        user.setRoles (roles);}

        userService.AdmSaveUserInfo (user);

        return ResponseEntity.ok(item);
    }

    ////////////////////////////CATEGORIES CRUD
    @GetMapping(value = "/allcategories")
    public ResponseEntity<?> getAllCats(){

        List<Categories> items = movieService.getAllCategories ();


        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @PostMapping(value = "/addcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> addCat(@RequestBody Categories item){
        movieService.addCategory (item);
        return ResponseEntity.ok(item);
    }

    @PutMapping(value = "/savecategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveCat(@RequestBody Categories item){
        movieService.saveCategory (item);
        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/getcategory/{id}")
    public ResponseEntity<?> getCat(@PathVariable(name = "id") Long id){
        Categories item = movieService.getCategory (id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/deletecategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> deleteCat(@RequestBody Categories item){
        Categories checkItem = movieService.getCategory (item.getId());
        if(checkItem!=null){
            movieService.deleteCategory (checkItem);
            return ResponseEntity.ok(checkItem);
        }
        return ResponseEntity.ok(item);
    }


    @PostMapping(value = "/assigncat")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> assigncat(@RequestBody AssignCatDTO item){


        Movies movie = movieService.getMovie (item.getMovie_id () );
        List<Categories> cats = movie.getCategories ();
        Categories addedCat =  movieService.getCategory (item.getCat_id ());

        if(cats.contains (addedCat))
        {
            cats.remove (addedCat);
        }
        else {
            cats.add (addedCat);

            movie.setCategories (cats);}

        movieService.saveMovie (movie);

        return ResponseEntity.ok(item);
    }

    ////////////////////////////HALLS CRUD
    @GetMapping(value = "/allhalls")
    public ResponseEntity<?> getAllHalls(){

        List<Halls> items = movieService.getAllHalls ();


        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/addhall")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> addCat(@RequestBody HallDTO item){

        GsonBuilder gsonMapBuilder = new GsonBuilder();

        Gson gsonObject = gsonMapBuilder.create();

        String JSONObject = gsonObject.toJson(item.getHallplaces ());

         Halls hh = new Halls ();
         hh.setPlaces (JSONObject);
         hh.setNumber ((long) item.getNumber ());
        movieService.saveHall (hh);

        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/gethall/{id}")
    public ResponseEntity<?> getHall(@PathVariable(name = "id") Long id){
        Halls item = movieService.getHall (id);
        if(item!=null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/savehall")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> saveHall(@RequestBody Halls item){
        movieService.saveHall (item);
        return ResponseEntity.ok(item);
    }



    @DeleteMapping(value = "/deletehall")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<?> deleteHall(@RequestBody Halls item){
        Halls checkItem = movieService.getHall (item.getId());
        if(checkItem!=null){
            movieService.deleteHall (checkItem);
            return ResponseEntity.ok(checkItem);
        }
        return ResponseEntity.ok(item);
    }
    /////////////////////RESERVE


     @Autowired
     private SendEmailService sendEmailService;
    
    @PutMapping(value = "/reserve")

    public ResponseEntity<?> reserve(@RequestBody ReserveDTO item) throws IOException, WriterException, MessagingException {

        System.out.println (item);
        Schedules sc = movieService.getSchedule (item.getItemId ());
        List<String> newPlaces = new ArrayList<> ();
        //sc places просто строка

        Gson gson = new Gson();

        Type userListType = new TypeToken<String[][]> (){}.getType();
       String[][] placesArray = gson.fromJson(sc.getPlaces (), userListType);

//        String[][] placesArrayEnd = new String[placesArray.length][placesArray[0].length];

        UUID uuid = UUID.randomUUID();
        String uuidAsString = item.getItemId ()+"$" + uuid.toString();
        System.out.println (uuidAsString);
        List<Tickets> buedticks = new ArrayList<> ();
        boolean succ = true;

        for (Tickets tick:
                item.getTickets ()  ) {

            for (int i = 0; i<placesArray.length; i++){

                for (int j = 0; j<placesArray[i].length; j++){
                    if(tick.getRow () == i+1 && tick.getCol () == j+1 && !placesArray[i][j].equals ("0")){

                        buedticks.add (tick);

                        succ = false;


                    }

                }


            }





        }

        if(succ){
        for (Tickets tick:
        item.getTickets ()  ) {

            for (int i = 0; i<placesArray.length; i++){

                for (int j = 0; j<placesArray[i].length; j++){
                    if(tick.getRow () == i+1 && tick.getCol () == j+1 && placesArray[i][j].equals ("0")){
                        placesArray[i][j] = uuidAsString;
                        if(tick.getType ()==1){
                            placesArray[i][j] +="1";
                        }
                        if(tick.getType ()==2){
                            placesArray[i][j] +="2";
                        }
                        if(tick.getType ()==3){
                            placesArray[i][j] +="3";
                        }

                    }

                }


            }





        }



        GsonBuilder gsonMapBuilder = new GsonBuilder();

        Gson gsonObject = gsonMapBuilder.create();

        String JSONObject = gsonObject.toJson(placesArray);
        sc.setPlaces (JSONObject);
        movieService.saveSchedule (sc);




        String str= "http://localhost:3000/checkqr/" + uuidAsString;
//path where we want to get QR Code
        String path = "target/classes/static/qr/"+uuidAsString +".png";
//Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//invoking the user-defined method that creates the QR code
        generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
//prints if the QR code is generated
        System.out.println("QR Code created successfully.");

        String time = sc.getTime ().toString ();
            time = time.substring(0, time.length() - 3);

        String body2= "<b>"+sc.getMovie ().getTitle () + "</b><br/> " + sc.getDay ().toString () + "<br/>  " + time + "<br/>  " + sc.getHall () + " hall <br/>";

        String body ="";
        int in = 1;
            for (Tickets tick: item.getTickets ()
                 ) {
                body+= ""+in + ") " + tick.getRow () + " row, " + tick.getCol () + " place. Tariff: <u>" + tick.getTypeStr () + "</u><br/>";
                in++;
            }
        String img ="";


        sendEmailService.sendEmailQr (item.getEmail (), "<div style='margin-left:250px'> <div class='col-6'></br>"+body2 + "Places successfully reserved:  <br/>"+body + "   <br/> </div> <div class='col-6'>"+ img + "</div></div>","Thank you for buying tickets on KinoLog", path);


        return ResponseEntity.ok(item);}
        else {
            return ResponseEntity.ok(buedticks);

        }
    }
    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException
    {
//the BitMatrix class represents the 2D matrix of bits
//MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter ().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File (path));
    }


    @GetMapping(value = "/checkqr/{qr}")
    public ResponseEntity<?> getCheck(@PathVariable(name = "qr") String qr){
        String id = "";


        for (int i =0; i<qr.length (); i++){



            if (qr.charAt (i) == '$' ) break;

            id = id + qr.charAt (i);


        }
        System.out.println ("id"  + id);

            Schedules sc = movieService.getSchedule (Long.parseLong (id));

            if (sc.getPlaces ().contains (qr) ){
                System.out.println ("contains");
                sc.setPlaces (sc.getPlaces ().replace (qr,"LIGHT"));

                return ResponseEntity.ok(sc);

            }



            return ResponseEntity.notFound().build();

    }




}
