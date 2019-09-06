<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="对应的用户ID">
              <a-input placeholder="请输入对应的用户ID" v-model="queryParam.userId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="userName">
              <a-input placeholder="请输入userName" v-model="queryParam.userName"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="用户性别(1:男 2:女)">
              <a-input placeholder="请输入用户性别(1:男 2:女)" v-model="queryParam.userSex"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="身份证号">
              <a-input placeholder="请输入身份证号" v-model="queryParam.userCard"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="职务">
              <a-input placeholder="请输入职务" v-model="queryParam.userPost"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('企业信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sysUserCompanyData-modal ref="modalForm" @ok="modalFormOk"></sysUserCompanyData-modal>
  </a-card>
</template>

<script>
  import sysUserCompanyDataModal from './modules/sysUserCompanyDataModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "sysUserCompanyDataList",
    mixins:[JeecgListMixin],
    components: {
      sysUserCompanyDataModal
    },
    data () {
      return {
        description: '企业信息管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '对应的用户ID',
            align:"center",
            dataIndex: 'userId'
           },
		   {
            title: 'userName',
            align:"center",
            dataIndex: 'userName'
           },
		   {
            title: '用户性别(1:男 2:女)',
            align:"center",
            dataIndex: 'userSex'
           },
		   {
            title: '身份证号',
            align:"center",
            dataIndex: 'userCard'
           },
		   {
            title: '职务',
            align:"center",
            dataIndex: 'userPost'
           },
		   {
            title: '职称',
            align:"center",
            dataIndex: 'userTitle'
           },
		   {
            title: '固定电话',
            align:"center",
            dataIndex: 'userTelephone'
           },
		   {
            title: '单位名称',
            align:"center",
            dataIndex: 'companyName'
           },
		   {
            title: '社会信用代码',
            align:"center",
            dataIndex: 'socialCreditCode'
           },
		   {
            title: '主管部门',
            align:"center",
            dataIndex: 'competentDepartment'
           },
		   {
            title: '单位性质',
            align:"center",
            dataIndex: 'unitNature'
           },
		   {
            title: '工商注册地',
            align:"center",
            dataIndex: 'incorporationPlace'
           },
		   {
            title: '公司地址-省',
            align:"center",
            dataIndex: 'companyAddressProvince'
           },
		   {
            title: '公司地址-市',
            align:"center",
            dataIndex: 'companyAddressCity'
           },
		   {
            title: '公司地址-详细地址',
            align:"center",
            dataIndex: 'companyDetailAddress'
           },
		   {
            title: '公司电话',
            align:"center",
            dataIndex: 'companyTelephone'
           },
		   {
            title: '公司传真',
            align:"center",
            dataIndex: 'companyFax'
           },
		   {
            title: '公司邮箱',
            align:"center",
            dataIndex: 'companyEmail'
           },
		   {
            title: '公司邮政编码',
            align:"center",
            dataIndex: 'companyPostalCode'
           },
		   {
            title: '公司银行账户',
            align:"center",
            dataIndex: 'companyBankAccount'
           },
		   {
            title: '公司银行账号',
            align:"center",
            dataIndex: 'companyBankCode'
           },
		   {
            title: '银行名称',
            align:"center",
            dataIndex: 'bankName'
           },
		   {
            title: '注册资本(万元)',
            align:"center",
            dataIndex: 'registeredCapital'
           },
		   {
            title: '资本类型（1.人民币 2.美元）',
            align:"center",
            dataIndex: 'capitalType'
           },
		   {
            title: '信用等级（1.AAA 2.AA 3.A）',
            align:"center",
            dataIndex: 'creditRating'
           },
		   {
            title: '公司成立时间',
            align:"center",
            dataIndex: 'companyCreateTime'
           },
		   {
            title: '经营范围',
            align:"center",
            dataIndex: 'businessScope'
           },
		   {
            title: '法人',
            align:"center",
            dataIndex: 'legalPerson'
           },
		   {
            title: '证件类型（1.身份证 2.护照 3.港澳台证）',
            align:"center",
            dataIndex: 'documentType'
           },
		   {
            title: '证件号码',
            align:"center",
            dataIndex: 'documentCode'
           },
		   {
            title: '法人手机号',
            align:"center",
            dataIndex: 'legalPhone'
           },
		   {
            title: '法人固定电话',
            align:"center",
            dataIndex: 'legalTelephone'
           },
		   {
            title: '法人邮箱',
            align:"center",
            dataIndex: 'legalEmail'
           },
		   {
            title: '学历(字典)',
            align:"center",
            dataIndex: 'legalEducation'
           },
		   {
            title: '学位',
            align:"center",
            dataIndex: 'legalAcademicDegree'
           },
		   {
            title: '法人职务',
            align:"center",
            dataIndex: 'legalPost'
           },
		   {
            title: '法人性别(字典)',
            align:"center",
            dataIndex: 'legalSex'
           },
		   {
            title: '法人出生日期',
            align:"center",
            dataIndex: 'legalBirthDate'
           },
		   {
            title: '法人职称(字典)',
            align:"center",
            dataIndex: 'legalTitle'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/system/sysUserCompanyData/list",
          delete: "/system/sysUserCompanyData/delete",
          deleteBatch: "/system/sysUserCompanyData/deleteBatch",
          exportXlsUrl: "system/sysUserCompanyData/exportXls",
          importExcelUrl: "system/sysUserCompanyData/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>