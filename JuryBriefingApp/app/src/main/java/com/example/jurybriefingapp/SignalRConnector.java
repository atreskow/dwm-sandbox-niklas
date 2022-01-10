package com.example.jurybriefingapp;

public class SignalRConnector {
/*
    public void Ping()
    {
        Clients.All.ping();
    }

    public void NextSlide(Guid tastingRoomId)
    {
        var tastingRoom = _service.GetTastingRoom((Guid)tastingRoomId);
        var currentSlide = tastingRoom.SLIDE;
        var slideIdx = Math.Min(NumSlides()-1, currentSlide + 1);
        var updateModel = new UpdateTastingRoomSlideModel
        {
            Slide = slideIdx
        };
        var res = _service.UpdateTastingRoomSlide(tastingRoomId, updateModel);
        Clients.All.showSlide(tastingRoomId, slideIdx);
    }
    public void PrevSlide(Guid tastingRoomId)
    {
        var tastingRoom = _service.GetTastingRoom((Guid)tastingRoomId);
        var currentSlide = tastingRoom.SLIDE;
        var slideIdx = Math.Max(0, currentSlide - 1);
        var updateModel = new UpdateTastingRoomSlideModel
        {
            Slide = slideIdx
        };
        _service.UpdateTastingRoomSlide(tastingRoomId, updateModel);
        Clients.All.showSlide(tastingRoomId, slideIdx);
    }

    public void StartTasting(Guid tastingRoomId)
    {
        //var cfg = ClientConfiguration.Instance;
        var updateModel = new UpdateTastingRoomStateModel
        {
            State = TastingRoomState.TASTING
        };
        _service.UpdateTastingRoomState(tastingRoomId, updateModel);
        //cfg.StartController = "App";
        Clients.All.goToStartPage(tastingRoomId);
    }

    private int NumSlides()
    {
        var path = (System.Web.HttpContext.Current == null)
                ? System.Web.Hosting.HostingEnvironment.MapPath("~/App_Data/Slides")
                : System.Web.HttpContext.Current.Server.MapPath("~/App_Data/Slides");
        var slides = System.IO.Directory.EnumerateFiles(path);
        return slides.Count();
    }

    public void LoadSlidesRequest()
    {
        var connectionId = Context.ConnectionId;
        var numSlides = _service.JuryBriefingSlidesCount();
        for (int slideId = 0; slideId < numSlides; slideId++)
        {
            var slide = JuryBriefingSlidesCache.LookUp(_service, slideId);
            SlideRequestQueue.PushRequest(connectionId, slideId, slide.SLIDE_DATA);
        }
    } */
}
