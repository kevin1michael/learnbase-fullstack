from django.contrib import admin

from .models import Story, Reader, StoryPage

admin.site.register(Story)
admin.site.register(Reader)
admin.site.register(StoryPage)
