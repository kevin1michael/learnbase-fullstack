import json

from django.contrib.auth.decorators import login_required
from django.shortcuts import render

from django.shortcuts import render
from django.http import HttpResponse

from .models import Story, Reader, StoryPage
from .stories import story1, story2, story3

def load_story(story):
    try:
        old_story = Story.objects.get(name=story['title'], image_url=story['image_url'])
        old_story.delete()
    except Story.DoesNotExist:
        pass

    new_story = Story(name=story['title'], image_url=story['image_url'])
    new_story.save()

    pages = story['pages']
    page_lookup = {}

    for page in pages:
        new_page = StoryPage()
        new_page.story = new_story
        new_page.image_url = page['image_url']
        new_page.text = page['text']
        new_page.save()

        page_lookup[page['page_id']] = {
            'page_obj': new_page,
            'json_obj': page
        }

    for page_id, value in page_lookup.items():
        json_obj = value['json_obj']
        page_obj = value['page_obj']

        if 'path_1_id' in json_obj and 'path_1_text' in json_obj:
            page_obj.path_1 = page_lookup[json_obj['path_1_id']]['page_obj']
            page_obj.text_1 = json_obj['path_1_text']

        if 'path_2_id' in json_obj and 'path_2_text' in json_obj:
            page_obj.path_2 = page_lookup[json_obj['path_2_id']]['page_obj']
            page_obj.text_2 = json_obj['path_2_text']

        page_obj.save()

    new_story.first_page = page_lookup[0]['page_obj']
    new_story.save()

@login_required()
def load_stories(request):
    #load_story(story1)
    load_story(story2)
    load_story(story3)
    return HttpResponse(status=200)

def get_stories(request):
    if request.method != 'GET':
        return HttpResponse(status=405)

    stories = Story.objects.all()
    stories = [story.as_json() for story in stories]

    response_data = json.dumps(stories)

    if "application/json" in request.META['HTTP_ACCEPT_ENCODING']:
        mimetype = 'application/json'
    else:
        mimetype = 'text/plain'
    return HttpResponse(response_data, content_type=mimetype)


def start_story(request):
    if request.method != 'POST':
        return HttpResponse(status=405)

    data = json.loads(request.body)

    if 'username' not in data or not data['username']:
        return HttpResponse(status=400)

    if 'story_id' not in data or not data['story_id']:
        return HttpResponse(status=400)

    username = data['username']
    story_id = data['story_id']

    try:
        reader = Reader.objects.get(username=username)
    except Reader.DoesNotExist:
        reader = Reader(username=username)
        reader.save()

    try:
        story = Story.objects.get(id=story_id)
    except Story.DoesNotExist:
        return HttpResponse(status=404)

    reader.reading = story.first_page
    reader.save()

    response_data = json.dumps(story.first_page.as_json())

    if "application/json" in request.META['HTTP_ACCEPT_ENCODING']:
        mimetype = 'application/json'
    else:
        mimetype = 'text/plain'
    return HttpResponse(response_data, content_type=mimetype)

def take_path(request):
    if request.method != 'POST':
        return HttpResponse(status=405)

    data = json.loads(request.body)

    if 'path_id' not in data or not data['path_id']:
        return HttpResponse(status=400)

    if 'username' not in data or not data['username']:
        return HttpResponse(status=400)

    path_id = data['path_id']
    username = data['username']

    try:
        reader = Reader.objects.get(username=username)
    except Reader.DoesNotExist:
        return HttpResponse(status=404)

    try:
        page = StoryPage.objects.get(id=path_id)
    except StoryPage.DoesNotExist:
        return HttpResponse(status=404)

    reader.reading = page
    reader.save()

    response_data = json.dumps(page.as_json())

    if "application/json" in request.META['HTTP_ACCEPT_ENCODING']:
        mimetype = 'application/json'
    else:
        mimetype = 'text/plain'
    return HttpResponse(response_data, content_type=mimetype)

def finish_story(request):
    if request.method != 'POST':
        return HttpResponse(status=405)

    data = json.loads(request.body)

    if 'username' not in data or not data['username']:
        return HttpResponse(status=400)

    username = data['username']

    try:
        reader = Reader.objects.get(username=username)
    except Reader.DoesNotExist:
        return HttpResponse(status=404)

    reader.reading = None
    reader.save()

    return HttpResponse(status=200)

def resume_story(request):
    if request.method != 'POST':
        return HttpResponse(status=405)

    data = json.loads(request.body)

    if 'username' not in data or not data['username']:
        return HttpResponse(status=400)

    username = data['username']

    try:
        reader = Reader.objects.get(username=username)
    except Reader.DoesNotExist:
        return HttpResponse(status=404)

    if not reader.reading:
        return HttpResponse(status=200)

    response_data = json.dumps(reader.reading.as_json())

    if "application/json" in request.META['HTTP_ACCEPT_ENCODING']:
        mimetype = 'application/json'
    else:
        mimetype = 'text/plain'
    return HttpResponse(response_data, content_type=mimetype)

# Create your views here.
