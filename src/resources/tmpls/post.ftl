[#ftl]
[#-- @implicitly included --]
[#-- @ftlvariable name="post" type="pvt.example.pojo.entity.Post" --]
<!DOCTYPE html>
<html lang="zh_CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
  <link rel="stylesheet" href="/static/sanitize.css">
  <link rel="stylesheet" href="/static/font-awesome/font-awesome.min.css">
  <link rel="stylesheet" href="/static/vditor/dist/index.css" />
  <script type="text/javascript" src="/database.js"></script>
  <script type="text/javascript" src="/static/utils.js"></script>
  <script type="text/javascript" src="/static/header.js"></script>
  <script type="text/javascript" src="/static/vditor/dist/method.js"></script>
  <script type="text/javascript" src="/static/sql/sql-wasm.min.js"></script>
  <link rel="stylesheet" href="/static/header.css">
  <link rel="stylesheet" href="/posts.css">
  <!-- TODO SEO设置 -->
  <title>${post.title!"title内容"}</title>
</head>
<body>
  <!-- 顶部栏 -->
  <div class="header">
    <img src="/static/img/default-logo.png" alt="logo" data-event="home">
    <div><span data-event="home">首页</span><span data-event="post">文章</span><span data-event="about">关于我</span></div>
    <span data-event="site">🏠主站</span>
  </div>
  <!-- 主要内容 -->
  <div class="contain">
    <!-- 文章目录索引 -->
    <div class="sidebar-toc">
      <div style="display: block;"></div>
    </div>
    <div class="sidebar-toc-btn">📇</div>
    <!-- 文章主要 -->
    <div class="post-main">
      <!-- 文章头信息 -->
      <div class="post-header">
        <div class="header-img">
          <img src="${post.cover!""}" alt="${post.title!"title内容"}" onerror="this.onerror=null;this.src='/static/img/default-cover.png'">
        </div>
        <div class="header-info">
          <h1 class="post-title">${post.title!"title内容"}</h1>
          <p class="post-summary">${post.summary!"summary内容"}</p>
          <div class="post-meta">
            <span><i class="fa fa-calendar">&ensp;</i><span>2025/01/01 12:00</span></span>
            <span><i class="fa fa-pencil">&ensp;</i><span>2025/01/01 12:00</span></span>
            <span><i class="fa fa-file-word-o">&ensp;</i><span>${post.content?replace("\\s+", "")?length!0}</span></span>
            <span><i class="fa fa-th-large">&ensp;</i><span>${post.category.name}</span></span>
            <span><i class="fa fa-tags">&ensp;</i><span>${post.tags?map(p -> p.name)?join(", ")}</span></span>
          </div>
        </div>
      </div>
      <!-- 文章内容 -->
      <div class="post-content">
        <div>${post.htmlContent!"htmlContent内容"}</div>
      </div>
    </div>
    <!-- 侧边栏作者信息 -->
    <div class="sidebar-author">
      <div class="aside-item-author">
        <div class="user">
          <img class="avatar" src="/static/img/default-avatar3.jpg" alt="头像">
          <div>
            <span>用户名</span>
            <p>谏言</p>
          </div>
        </div>
        <div class="count">
          <div class="item"><span class="num">0</span><span>文章数</span></div>
          <div class="item"><span class="num">0</span><span>分类数</span></div>
          <div class="item"><span class="num">0</span><span>标签数</span></div>
        </div>
      </div>
      <div class="aside-item-clock">N年N天N时N分</div>
      <div class="aside-random-post"></div>
    </div>
  </div>
  <script type="text/javascript">
    const initMarkdown = `${post.content?replace("`","\\`")!"content内容"}`
  </script>
  <script type="text/javascript" src="/posts.js"></script>
</body>
</html>
