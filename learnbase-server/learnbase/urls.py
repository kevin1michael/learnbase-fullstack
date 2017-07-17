"""helloworld URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin

from stories import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),

    ### Story API ###
    url(r'^get_stories/', views.get_stories),
    url(r'^start_story/', views.start_story),
    url(r'^take_path/', views.take_path),
    url(r'^finish_story/', views.finish_story),
    url(r'^resume_story/', views.resume_story),

    ### Management ###
    url(r'^load_stories/', views.load_stories),
]
