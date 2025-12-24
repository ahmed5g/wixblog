import {Routes} from '@angular/router';
import {TermsAndConditions} from './util/terms-and-conditions';
import {About} from './util/about';
import {PrivacyPolicy} from './util/privacy-policy';
import {Contact} from './util/contact';
import {PostDetails} from './post/post-details';
import {Profile} from './user/profile';
import {TopicsExplorer} from './topics/topics-explorer';
import {Topic} from './topics/topic';


export const Pages: Routes=[
  {
    path: "terms&conditions", component: TermsAndConditions
  },
  {
    path: "privacypolicy", component: PrivacyPolicy
  },
  {
    path: "post/:slug", component: PostDetails
  },
  {
    path: "contact", component: Contact
  },
  {path: "about", component: About},
  {path: "topics", component: TopicsExplorer},
  {path: "singletopic", component: Topic},

  {path: "profile/:provider", component: Profile},

]
