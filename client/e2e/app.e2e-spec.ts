import { OVOgotchiPage } from './app.po';

describe('ovogotchi App', function() {
  let page: OVOgotchiPage;

  beforeEach(() => {
    page = new OVOgotchiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
