from __future__ import unicode_literals

from django.db import models

class Story(models.Model):
    name = models.TextField()

    image_url = models.TextField(blank=True)
    first_page = models.ForeignKey('StoryPage', related_name='back_title', null=True, blank=True)

    def as_json(self):
        return dict(
            story_id=self.id, story_name=self.name, image_url=self.image_url,
            first_page_id=self.first_page.id)

class StoryPage(models.Model):
    story = models.ForeignKey(Story)

    image_url = models.TextField(blank=True)
    text = models.TextField()

    path_1 = models.ForeignKey('self', null=True, blank=True, related_name='back_1')
    path_2 = models.ForeignKey('self', null=True, blank=True, related_name='back_2')

    text_1 = models.TextField(blank=True)
    text_2 = models.TextField(blank=True)

    def as_json(self):
        dictionary = dict(
            page_id=self.id, image_url=self.image_url,
            text=self.text)

        if self.path_1 and self.text_1:
            dictionary['path_1_id'] = self.path_1.id
            dictionary['path_1_text'] = self.text_1

        if self.path_2 and self.text_2:
            dictionary['path_2_id'] = self.path_2.id
            dictionary['path_2_text'] = self.text_2

        return dictionary


class Reader(models.Model):
    username = models.TextField()
    reading = models.ForeignKey(StoryPage, null=True, blank=True)
