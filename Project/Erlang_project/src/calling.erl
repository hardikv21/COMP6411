%%%-------------------------------------------------------------------
%%% @author hardi
%%% @copyright (C) 2020, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 13. Jun 2020 11:53
%%%-------------------------------------------------------------------
-module(calling).
-author("hardi").

%% API
-export([baz2/4]).

baz2(Name, Contact,Masterid, Counter) ->
  if Counter == 0 ->
      lists:foreach(fun(N) ->
        whereis(N)!{Name,intro,element(3,now())}

      end, Contact),

      baz2(Name,Contact, Masterid, Counter+ 10);

    Counter > 0 ->
      receive
        {Sender,intro,Timestamp} ->
          Masterid!{intro,Name,Sender,Timestamp},
          whereis(Sender)!{Name,reply,Timestamp},
          baz2(Name,Contact, Masterid, Counter+ 1001);

        {Sender,reply,Timestamp} ->
          Masterid!{reply,Name,Sender,Timestamp},
          baz2(Name,Contact, Masterid, Counter+ 10000)
      after
        5000->
          io:fwrite("~nProcess ~p has received no calls for 5 second, ending...~n",[Name])
      end
  end.