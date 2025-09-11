// src/resources/static/views/content/home.js
;(function () {
  const $ = layui.jquery, laytpl = layui.laytpl
  countTotal()
  hotMap()
  pieCategoryTag()
  systemInfoRender()
  /** 总共数量统计(header) */
  function countTotal() {
    const $header = $('.header')
    $.ajax({
      url: ctxUrl + "basic/count-total", type: "GET", success(res) {
        const {code, data} = res
        if (code === 200) {
          $header.html(`
          <div class="count"><div><span>文章数</span><span>${ data['post'] }</span></div><div><i class="fa fa-newspaper-o"></i></div></div>
          <div class="count"><div><span>分类数</span><span>${ data['category'] }</span></div><div><i class="fa fa-coffee"></i></div></div>
          <div class="count"><div><span>标签数</span><span>${ data['tag'] }</span></div><div><i class="fa fa-tags"></i></div></div>
          <div class="count"><div><span>回收站</span><span>${ data['recycle'] }</span></div><div><i class="fa fa-recycle"></i></div></div>
          <div class="count"><div><span>图片数</span><span>${ data['image'] }</span></div><div><i class="fa fa-image"></i></div></div>`)
        }
      },
    })
  }
  /** 日历热力图(eCharts) */
  function hotMap() {
    const myChart = echarts.init(document.querySelector('.calendar-hot-map>div')), year = new Date().getFullYear()
    $.ajax({
      url: ctxUrl + "basic/count-post-day", type: "GET", data: {year}, success(res) {
        if (res.code === 200) {
          const data = res.data.map(item => [item['day_date'], item['post_count']])
            , option = {
            calendar: {top: 60, left: 30, right: 30, range: year, yearLabel: {show: true}},
            tooltip: {}, formatter: '{c}', series: [{type: 'heatmap', coordinateSystem: 'calendar', data}],
          }
          myChart.setOption(option)
        }
      },
    })
  }
  /** category和tag(eCharts) 饼图 */
  function pieCategoryTag() {
    const myChart1 = echarts.init(document.querySelector('.category-pie-chart>div'))
      , myChart2 = echarts.init(document.querySelector('.tag-pie-chart>div'))
    $.ajax({
      url: ctxUrl + "basic/count-category-tag",
      type: "GET",
      success(res) {
        if (res.code === 200) {
          const option1 = {
            legend: {icon: 'circle', orient: 'vertical', left: 'right', data: res.data['category'].map(item => item['category'])},
            series: [{
              type: 'pie',
              radius: ['25%', '50%'],
              center: ['50%', '58%'],
              labelLine: {show: true},
              data: res.data['category'].map(item => ({name: item['category'], value: item['value']})),
              label: {formatter: function (data) {return `${ data.name } ${ data.value }` }},
            }],
          }, option2 = {
            legend: {icon: 'circle', orient: 'vertical', left: 'right', data: res.data['tag'].map(item => item['tag'])},
            series: [{
              type: 'pie',
              radius: ['25%', '50%'],
              center: ['50%', '58%'],
              labelLine: {show: true},
              data: res.data['tag'].map(item => ({name: item['tag'], value: item['value']})),
              label: {formatter: function (data) {return `${ data.name } ${ data.value }` }},
            }],
          }
          myChart1.setOption(option1)
          myChart2.setOption(option2)
        }
      },
    })
  }
  /** 系统信息统计 */
  function systemInfoRender() {
    $.ajax({
      url: ctxUrl + "basic/system-info",
      type: "GET",
      success(res) {
        if (res.code === 200) {
          const {jvm, memoryDisk, os, cpu} = res.data
          const cpuInfo = {title: "CPU信息", list: Object.entries(cpu).map(([k, v]) => ({name: key2Value(k), value: v}))}
            , osInfo = {title: "操作系统信息", list: Object.entries(os).map(([k, v]) => ({name: key2Value(k), value: v}))}
            , jvmInfo = {title: "JVM信息", list: Object.entries(jvm).map(([k, v]) => ({name: key2Value(k), value: v}))}
            , memoryDiskInfo = {title: "内存信息", list: Object.entries(memoryDisk).map(([k, v]) => ({name: key2Value(k), value: v}))}
          const tpl = document.getElementById('systemInfo').innerHTML
            , cpuView = document.querySelector('.footer>.item.cpu')
            , osView = document.querySelector('.footer>.item.os')
            , jvmView = document.querySelector('.footer>.item.jvm')
            , memoryDiskView = document.querySelector('.footer>.item.memory')
          laytpl(tpl).render(cpuInfo, function (html) {cpuView.innerHTML = html})
          laytpl(tpl).render(osInfo, function (html) {osView.innerHTML = html})
          laytpl(tpl).render(jvmInfo, function (html) {jvmView.innerHTML = html})
          laytpl(tpl).render(memoryDiskInfo, function (html) {memoryDiskView.innerHTML = html})
        }
      },
    })
  }
  /**
   * 字典K=>V
   * @param {string} key 键
   */
  function key2Value(key) {
    const KV = {
      cpuLoad: "CPU负载", cpuMaxFreq: "CPU最大频率", cpuNowFreq: "CPU当前频率", cpuCore: "CPU核心数",
      diskAvailable: "硬盘可用率", diskTotal: "硬盘总空间", memoryAvailable: "内存可用率", memoryTotal: "内存总空间",
      jvmMax: "JVM最大内存", jvmVendor: "JVM供应商", jvmHome: "Java路径", jvmTotal: "JVM当前最大内存", jvmVersion: "Java版本",
      jvmTime: "JVM运行时间", jvmName: "JVM名称", jvmFree: "JVM可用内存",
      osArch: "系统架构", osTime: "系统运行时间", osType: "操作系统类型", osName: "设备名称", osVendor: "系统供应商",
    }
    if (Object.keys(KV).includes(key)) { return /**@type {string}*/KV[key]} else { return "未知属性"}
  }
})()