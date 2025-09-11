// upload/database.js
const dbJson = {
  initPageId: 21,
  menuData: [
    {id: 0, name: "首页", icon: "fa-home", path: "content/home.html", isExtLink: false},
    {
      id: 1, name: "博客管理", icon: "fa-map-signs", isExtLink: false,
      children: [
        {id: 11, name: "文章管理", icon: "fa-book", path: "content/posts.html", isExtLink: false},
        {id: 12, name: "分标管理", icon: "fa-tags", path: "content/categories_tags.html", isExtLink: false},
      ],
    },
    {
      id: 2, name: "基础管理", icon: "fa-info", isExtLink: false,
      children: [
        {id: 21, name: "基础配置", icon: "fa-fire", path: "content/setting.html", isExtLink: false},
        {id: 22, name: "图片管理", icon: "fa-image", path: "content/image.html", isExtLink: false},
        {id: 23, name: "回收站", icon: "fa-recycle", path: "content/recycle.html", isExtLink: false},
      ],
    },
    {id: 3, name: "跳转主页", icon: "fa-external-link-square", path: "https://www.example.com/", isExtLink: true},
    {id: 4, name: "跳转博客", icon: "fa-road", path: "https://www.example.com/", isExtLink: true},
  ],
}